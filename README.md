# FlatEarth

**FlatEarth** is a Minecraft server plugin.  
It alters the map generation of your Minecraft server such that it generates an approximately 1:1-scale flat copy of
the real Earth, using OpenStreetMap data. Useful when you want to recreate real-world buildings or structures in
Creative Mode (maybe even entire towns). **Not intended for Survival Mode at all!**

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
