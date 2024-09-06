package gr.kendrick.flat_earth;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.awt.Color;

public class FlatEarthWorldGenerator extends ChunkGenerator {

    final private int zoomLevel;
    final private String tileServerTemplateURL;
    final private int tileSizeInPx;
    final private String userAgent;

    public FlatEarthWorldGenerator(int zoomLevel, String tileServerTemplateURL, int tileSizeInPx, String userAgent) {
        super();
        this.zoomLevel = zoomLevel;
        this.tileServerTemplateURL = tileServerTemplateURL;
        this.tileSizeInPx = tileSizeInPx;
        this.userAgent = userAgent;
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new BiomeProvider() {
            @Override
            public Biome getBiome(WorldInfo worldInfo, int i, int i1, int i2) {
                return Biome.PLAINS; // ToDo: use water and Biome.OCEAN for oceans when ocean config is set to true!
            }

            @Override
            public List<Biome> getBiomes(WorldInfo worldInfo) {
                List<Biome> biomes = new ArrayList<>();
                biomes.add(Biome.PLAINS);
                return biomes;
            }
        };
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        ChunkData chunkData = createChunkData(world);

        // Set the entire chunk to a specific biome
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomeGrid.setBiome(x, z, Biome.PLAINS);
            }
        }

        // Generate a flat terrain at y = 64:
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 64, z, getBlockAt(chunkX, chunkZ, x, z)); // Top layer (Terracotta)
                for (int y = 1; y <= 63; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE); // Stone layer
                }
                chunkData.setBlock(x, 0, z, Material.BEDROCK); // Bedrock layer
            }
        }

        return chunkData;

    }

    private Material getBlockAt(int chunkX, int chunkZ, int x, int z) {
        int minecraftX = 16 * chunkX + x;
        int minecraftZ = 16 * chunkZ + z;
        return getBlockAt(minecraftX, minecraftZ);
    }

    private static final HashMap<Color, Material> colorToMaterialMap; // ToDo: allow user to custom set this via config
    static {
        colorToMaterialMap = new HashMap<Color, Material>();
        colorToMaterialMap.put(Color.BLACK, Material.BLACK_TERRACOTTA);
        colorToMaterialMap.put(Color.WHITE, Material.WHITE_TERRACOTTA);
        colorToMaterialMap.put(Color.GRAY, Material.GRAY_TERRACOTTA);
        colorToMaterialMap.put(Color.RED, Material.RED_TERRACOTTA);
        colorToMaterialMap.put(new Color(0x90, 0xEE, 0x90), Material.LIME_TERRACOTTA); // CSS "LightGreen"
        colorToMaterialMap.put(new Color(0, 0x64, 0), Material.GREEN_TERRACOTTA); // CSS "DarkGreen"
        colorToMaterialMap.put(new Color(0xAD, 0xD8, 0xE6), Material.LIGHT_BLUE_TERRACOTTA); // CSS "LightBlue"
        colorToMaterialMap.put(new Color(0, 0, 0x8B), Material.BLUE_TERRACOTTA); // CSS "DarkBlue"
        colorToMaterialMap.put(Color.PINK, Material.PINK_TERRACOTTA);
        colorToMaterialMap.put(Color.CYAN, Material.CYAN_TERRACOTTA);
        colorToMaterialMap.put(Color.ORANGE, Material.ORANGE_TERRACOTTA);
        colorToMaterialMap.put(Color.YELLOW, Material.YELLOW_TERRACOTTA);
    }

    public static double colorDistance(Color color1, Color color2) {
        if (color1 == null || color2 == null) {
            return Integer.MAX_VALUE;
        }
        return Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }

    public static Color closestAvailableColor(Color trueColor) {
        Color closestColor = null;
        for (Color color : colorToMaterialMap.keySet()) {
            if (colorDistance(trueColor, color) < colorDistance(trueColor, closestColor)) {
                closestColor = color; // update current knowledge of closestColor to trueColor
            }
        }
        return closestColor;
    }

    private Material getBlockAt(int minecraftX, int minecraftZ) {
        Color trueColor = getColorAt(minecraftX, minecraftZ);
        Color closestColor = closestAvailableColor(trueColor);
        return colorToMaterialMap.get(closestColor);
    }

    public Color getColorAt(int minecraftX, int minecraftZ) {
        MinecraftCoordinates minecraftCoordinates = new MinecraftCoordinates(minecraftX, minecraftZ);
        OSMCoordinates osmCoordinates = minecraftCoordinates.osm(this.zoomLevel, this.tileSizeInPx);

        if (osmCoordinates == null) {
            return Color.BLACK; // ToDo: allow user to set border of world behavior (e.g., void instead)
        }

        int osmTileX = osmCoordinates.osmTileX;
        int osmTileY = osmCoordinates.osmTileY;
        int pixelXWithinTile = osmCoordinates.pixelXWithinTile;
        int pixelYWithinTile = osmCoordinates.pixelYWithinTile;

        return new OSMTile(this.tileServerTemplateURL, userAgent, this.zoomLevel, osmTileX, osmTileY)
                .getColorRelativeToUpperLeftOrigin(pixelXWithinTile, pixelYWithinTile);
        // We assume each OSMTile to have dimensions 256x256 pixel where 1 pixel == 1 Minecraft block.
    }
}
