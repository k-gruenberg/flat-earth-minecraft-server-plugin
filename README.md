# FlatEarth

**FlatEarth** is a Minecraft server plugin.  
It alters the map generation of your Minecraft server such that it generates an approximately 1:1-scale flat copy of
the real Earth, using OpenStreetMap data. Useful when you want to recreate real-world buildings or structures in
Creative Mode (maybe even entire towns). **Not intended for Survival Mode at all!**

Also supports smaller than 1:1 scales!

## Images

### Zoom level 0:

![Minecraft Earth at zoom level 0](/zoom_level_0.png)

### Zoom level 1:

![Minecraft Earth at zoom level 0](/zoom_level_1.png)

### Zoom level 2:

![Minecraft Earth at zoom level 0](/zoom_level_2.png)

### Zoom level 17 (default, 1 block is approximately 1 meter):

![Minecraft Earth at zoom level 0](/zoom_level_17.png)

## Commands

### /whereami

Usage:
```
/whereami
```
Example output:
```
You're currently @
Place name: New York, United States
Latitude: 40.70287885589866
Longitude: -74.01384115219116
```

### /whereis

Usage:
```
/whereis <player>
```

### /tpcoords

Usage:
```
/tpcoords <latitude> <longitude>
```
Example usage:
```
/tpcoords 40.702875 -74.013841
```

### /tpplace

Usage:
```
/tpplace <place_name>
```
Example usage:
```
/tpplace New York City
```

## Config

Default configuration values (`config.yml`):

```
osm_zoom_level: 17
osm_tile_server_url: https://a.tile.openstreetmap.de/${z}/${x}/${y}.png
nominatim_url: https://nominatim.openstreetmap.org/search?q={QUERY}&format=xml&limit=1
nominatim_reverse_url: https://nominatim.openstreetmap.org/reverse?format=xml&lat={lat}&lon={long}&zoom=5&addressdetails=0
osm_tile_size_in_px: 256
user_agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML,
  like Gecko) Chrome/127.0.0.0 Safari/537.36
user_agent_nominatim: Mozilla/5.0 (Java HttpURLConnection; FlatEarth Minecraft server
  plugin)
world_name: flat_earth
color_to_material_mappings: '#000000=BLACK_TERRACOTTA,#ffffff=WHITE_TERRACOTTA,#808080=GRAY_TERRACOTTA,#ff0000=RED_TERRACOTTA,#90ee90=LIME_TERRACOTTA,#006400=GREEN_TERRACOTTA,#add8e6=LIGHT_BLUE_TERRACOTTA,#00008b=BLUE_TERRACOTTA,#ffafaf=PINK_TERRACOTTA,#00ffff=CYAN_TERRACOTTA,#ffc800=ORANGE_TERRACOTTA,#ffff00=YELLOW_TERRACOTTA'
ground_level: 64
underground_block: STONE
```

## Releasing Plugin

    mvn clean package

You should now have your new plugin jar file in `target` folder.

## To install on your Spigot compatible Minecraft Server

Copy `target/FlatEarth-n.n.n.jar` to your server `/plugin` folder, and reload server configuration
(or just restart server).

You should see these two messages in your server console:

```
[11:50:26] [Server thread/INFO]: [FlatEarth] Enabling FlatEarth v0.0.1
[11:50:26] [Server thread/INFO]: [FlatEarth] FlatEarth plugin enabled.
```
