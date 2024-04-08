#!/usr/bin/python
# -*- coding: UTF-8 -*-

import sys

sys.path.append("C:/codes/Java/Projects/GodSimulator/Python")
from LocalManager import LocalManager

if __name__ == '__main__':
    manager = LocalManager("C:/codes/Java/Projects/GodSimulator/out/GodSimulator.jar", "GodSimulator")
    manager.update()
