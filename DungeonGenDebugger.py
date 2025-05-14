import json
from matplotlib import ticker
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle
from matplotlib.lines import Line2D

# Load your JSON
with open("debug_dungeon.json", "r") as f:
    data = json.load(f)

floor = data["floors"][0]["grid"]

# Define colors for each cell type
type_colors = {
    "NORMAL": "#888888",
    "START": "#00cc44",
    "END": "#cc0000",
    "EMPTY": "#ffffff",
}

# Define colors for side types
side_colors = {
    "WALL": "#000000",
    "DOOR": "#ffaa00",
    "NONE": "#ffffff"
}

grid_size_y = len(floor)
grid_size_x = len(floor[0]) if grid_size_y > 0 else 0

fig, ax = plt.subplots(figsize=(grid_size_x/2, grid_size_y/2))

for y in range(grid_size_y):
    for x in range(grid_size_x):
        cell = floor[y][x]
        if cell is None:
            continue

        cell_type = cell["type"]
        sides = cell["sides"]

        # Draw cell fill
        color = type_colors.get(cell_type, "#cccccc")
        ax.add_patch(Rectangle((x, y), 1, 1, color=color, ec="gray"))

        if cell["type"] == "START":
            ax.text(x + 0.5, y + 0.5, "S", ha='center', va='center')
        elif cell["type"] == "END":
            ax.text(x + 0.5, y + 0.5, "E", ha='center', va='center')

        # Draw side lines
        line_width = 2
        half = 0.5
        cx, cy = x + half, y + half

        if sides[2] != "NONE":  # NORTH → y (top)
            ax.add_line(Line2D([x, x + 1], [y + 1, y + 1], color=side_colors.get(sides[2], "#000"), linewidth=line_width))
        if sides[1] != "NONE":  # EAST → x+1 (right)
            ax.add_line(Line2D([x + 1, x + 1], [y, y + 1], color=side_colors.get(sides[1], "#000"), linewidth=line_width))
        if sides[0] != "NONE":  # SOUTH → y (bottom)
            ax.add_line(Line2D([x, x + 1], [y, y], color=side_colors.get(sides[0], "#000"), linewidth=line_width))
        if sides[3] != "NONE":  # WEST → x (left)
            ax.add_line(Line2D([x, x], [y, y + 1], color=side_colors.get(sides[3], "#000"), linewidth=line_width))

ax.set_xlim(0, grid_size_x)
ax.set_ylim(0, grid_size_y)
ax.xaxis.set_major_locator(ticker.MultipleLocator(1))
ax.yaxis.set_major_locator(ticker.MultipleLocator(1))
ax.set_aspect("equal")
plt.gca().invert_yaxis()
plt.grid()
#plt.axis("off")
plt.title("Dungeon Floor Visualization")
plt.tight_layout()
plt.show()

