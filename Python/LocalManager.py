#!/usr/bin/python
# -*- coding: UTF-8 -*-

import shutil
from NDKLib import *
import time as t
import datetime
import time

from minecraft import *


# noinspection PyInitNewSignature
class LocalManager:

    def __init__(self, plugins_dir: str, plugin_name: str, plugin_version: str = "1.0.0"):

        self.plugins_dir = plugins_dir
        self.plugin_version = plugin_version
        self.plugin_name = plugin_name
        self.plugin_name_with_version = f"{plugin_name}-{plugin_version}"
        self.plugin_file_name = f"{self.plugin_name_with_version}.jar"
        self.plugin_local_file_path = fr"{plugins_dir}/{self.plugin_file_name}"
        self.server_dir = fr"\\wsl.localhost\Ubuntu\var\lib\pterodactyl\volumes\8ede2454-5c5a-4ca7-949f-58e99a16681e"
        self.server_plugins_dir = fr"{self.server_dir}\plugins"
        self.plugin_remote_file_path = fr"{self.server_plugins_dir}\{self.plugin_file_name}"

        self.server = ServerConnection("172.30.236.206", 25566, "8M1LjY0lbGy6bucJX5")

    def unLoadPlugin(self):
        debug(self.plugin_name)
        if self.plugin_name == "NDKPlugin":
            self.server.execute_command("ndk cancelnotify")
        self.server.execute_command(f"plugman unload {self.plugin_name}")
        debug("Отправлена команда на выгрузку плагина.")

    def loadPlugin(self):
        debug(self.plugin_name_with_version)
        self.server.execute_command(f"plugman load {self.plugin_name_with_version}")
        if self.plugin_name == "NDKPlugin":
            t.sleep(5)
            self.server.execute_command("ndk cancelnotify")
        debug("Отправлена команда на загрузку плагина.")

    def reloadPlugin(self):
        debug(self.server.execute_command(f"plugman reload {self.plugin_name}"))
        debug("Отправлена команда на перезагрузку плагина.")

    # def reloadPlugin(self):
    #     server.execute_command(f"plugman reload {self.plugin_name}")
    #     print("Отправлена команда на перезагрузку плагина.")

    def deleteFromServer(self):
        debug(self.plugin_file_name)
        os.remove(self.plugin_remote_file_path)
        debug("Плагин удалён с сервера.")

    def putToServer(self):
        shutil.copy(self.plugin_local_file_path, self.plugin_remote_file_path)
        # shutil.copyfile("C:/codes/Python/Projects/QT_Balance/balance.db", f"{self.server_plugins_dir}/NDKPlugin/balance.db")
        debug("Плагин загружен на сервер.")

    def update(self):
        # self.unLoadPlugin()
        # # t.sleep(1)
        # self.deleteFromServer()
        # self.putToServer()
        # self.loadPlugin()
        # # self.reloadPlugin()

        self.putToServer()
        self.unLoadPlugin()
        self.loadPlugin()


def debug(message: str):
    print(f"[{datetime.datetime.now()}] {message}")
