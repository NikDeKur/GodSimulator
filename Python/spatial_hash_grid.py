from typing import *

class SpatialHashGrid:
    def __init__(self, cell_size):
        self.cell_size = cell_size
        self.grid: Dict[Tuple[int, int, int], List["Structure"]] = {}

    def insert_structure(self, structure):
        min_x, min_y, min_z = structure.min_location
        max_x, max_y, max_z = structure.max_location

        for x in range(min_x, max_x + 1):
            for y in range(min_y, max_y + 1):
                for z in range(min_z, max_z + 1):
                    cell_key = self._calculate_cell_key(x, y, z)
                    if cell_key not in self.grid:
                        self.grid[cell_key] = []
                    self.grid[cell_key].append(structure)

    def query_location(self, location):
        x, y, z = location
        cell_key = self._calculate_cell_key(x, y, z)
        structures = self.grid.get(cell_key, [])

        return [structure for structure in structures if structure.contains(location)]

    def _calculate_cell_key(self, x, y, z):
        return x // self.cell_size, y // self.cell_size, z // self.cell_size


class Structure:
    def __init__(self, min_location, max_location):
        self.min_location = min_location
        self.max_location = max_location

    def contains(self, location):
        x, y, z = location
        min_x, min_y, min_z = self.min_location
        max_x, max_y, max_z = self.max_location

        return min_x <= x <= max_x and min_y <= y <= max_y and min_z <= z <= max_z

    def __repr__(self):
        return f"Structure({self.min_location}, {self.max_location})"


# Example usage:
grid = SpatialHashGrid(cell_size=10)

# Create structures
# stone_structure = Structure((127, 151, 105), (131, 155, 109))
wood_structure = Structure((50, 50, 50), (55, 55, 55))

# Insert structures into the grid
# grid.insert_structure(stone_structure)
grid.insert_structure(wood_structure)

# Query a location
print((56 // 10, 55 // 10, 55 // 10))
location = (56, 55, 55)
structures_containing_location = grid.query_location(location)
for e in structures_containing_location:
    print(e)
