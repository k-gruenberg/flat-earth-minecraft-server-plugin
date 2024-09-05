package gr.kendrick.flat_earth;

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
                chunkData.setBlock(x, 64, z, getBlockAt(chunkX, chunkZ, x, z)); // Top layer
                chunkData.setBlock(x, 63, z, Material.DIRT); // Dirt layer
                chunkData.setBlock(x, 62, z, Material.DIRT); // Dirt layer
                for (int y = 0; y <= 61; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE); // Stone layer
                }
            }
        }

        return chunkData;

    }

    private Material getBlockAt(int chunkX, int chunkZ, int x, int z) {
        int minecraftX = 16 * chunkX + x;
        int minecraftZ = 16 * chunkZ + z;
        return getBlockAt(minecraftX, minecraftZ);
    }

    private static final HashMap<Color, Material> colorToMaterialMap;
    static {
        colorToMaterialMap = new HashMap<Color, Material>();
        colorToMaterialMap.put(Color.BLACK, Material.BLACK_TERRACOTTA);
        colorToMaterialMap.put(Color.WHITE, Material.WHITE_TERRACOTTA);
        colorToMaterialMap.put(Color.RED, Material.RED_TERRACOTTA);
        colorToMaterialMap.put(Color.GREEN, Material.GREEN_TERRACOTTA);
        colorToMaterialMap.put(Color.BLUE, Material.BLUE_TERRACOTTA);
        colorToMaterialMap.put(Color.PINK, Material.PINK_TERRACOTTA);
        colorToMaterialMap.put(Color.CYAN, Material.CYAN_TERRACOTTA);
        colorToMaterialMap.put(Color.ORANGE, Material.ORANGE_TERRACOTTA);
        colorToMaterialMap.put(Color.YELLOW, Material.YELLOW_TERRACOTTA);
    }

    private int colorDistance(Color color1, Color color2) {
        if (color1 == null || color2 == null) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(color1.getRed() - color2.getRed())
                + Math.abs(color1.getGreen() - color2.getGreen())
                + Math.abs(color1.getBlue() - color2.getBlue());
    }

    private Material getBlockAt(int minecraftX, int minecraftZ) {
        Color trueColor = getColorAt(minecraftX, minecraftZ);
        Color closestColor = null;
        for (Color color : colorToMaterialMap.keySet()) {
            if (colorDistance(trueColor, color) < colorDistance(trueColor, closestColor)) {
                closestColor = color; // update current knowledge of closestColor to trueColor
            }
        }
        return colorToMaterialMap.get(closestColor);
    }

    private Color getColorAt(int minecraftX, int minecraftZ) {
        // For zoom level 1 there are 4 tiles (z,x,y given):
        // ___________________________________
        // |  (1,0,0)        |  (1,1,0)      |
        // |  North America  |  Asia         |
        // |                 |               |
        // |-----------------+----------------  // <--- we want the Minecraft origin (0,0) to be at the little +
        // |  (1,0,1)        |  (1,1,1)      |
        // |  South America  |  Australia    |
        // |                 |               |
        // |__________________________________

        int noOfTilesAlongOneAxis = (int) Math.pow(2, this.zoomLevel);
        // zoom level | noOfTilesAlongOneAxis
        // -----------+----------------------
        //  0         | 1
        //  1         | 2 (see above)
        //  2         | 4
        // ...        | ...
        // 20         | 1.048.576
        // => cf. https://wiki.openstreetmap.org/wiki/Zoom_levels

        int totalWorldWidthInBlocks = this.tileSizeInPx * noOfTilesAlongOneAxis; // = also the total world height

        // Transform (minecraftX, minecraftZ) coordinates into a (x,y) coordinate system with non-negative coordinates
        //   and the origin in the upper-left corner:
        // +----------------------------------> x
        // |
        // |
        // |
        // y
        int x = (totalWorldWidthInBlocks/2) + minecraftX; // transforms (-w/2, +w/2) range into (0, w) range
        int y = (totalWorldWidthInBlocks/2) + minecraftZ;

        if (x < 0 || y < 0 || x >= totalWorldWidthInBlocks || y >= totalWorldWidthInBlocks) {
            return Color.BLACK; // ToDo: allow user to set border of world behavior (e.g., void instead)
        }

        int osmTileX = x / this.tileSizeInPx;
        int osmTileY = y / this.tileSizeInPx;
        int pixelXWithinTile = x % this.tileSizeInPx;
        int pixelYWithinTile = y % this.tileSizeInPx;
        return new OSMTile(this.tileServerTemplateURL, userAgent, zoomLevel, osmTileX, osmTileY)
                .getColorRelativeToUpperLeftOrigin(pixelXWithinTile, pixelYWithinTile);
        // We assume each OSMTile to have dimensions 256x256 pixel where 1 pixel == 1 Minecraft block.
    }
}
