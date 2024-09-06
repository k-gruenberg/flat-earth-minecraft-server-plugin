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
