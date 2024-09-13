package gr.kendrick.flat_earth;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.Material;

import java.util.*;
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

        // Generate a flat terrain:
        int groundLevel = App.config.getInt("ground_level"); // Default: 64
        String undergroundBlock = App.config.getString("underground_block");
        Material undergroundMaterial = Optional.ofNullable(Material.getMaterial(
                Optional.ofNullable(undergroundBlock).orElse("STONE"))).orElse(Material.STONE);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Generate top layer (Terracotta blocks at default settings):
                chunkData.setBlock(x, groundLevel, z, getBlockAt(chunkX, chunkZ, x, z));

                // Generate all layers below / underground:
                for (int y = 1; y < groundLevel; y++) {
                    chunkData.setBlock(x, y, z, undergroundMaterial); // Stone layer (default)
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

    private Material getBlockAt(int minecraftX, int minecraftZ) {
        Color trueColor = getColorAt(minecraftX, minecraftZ);
        return App.colorToMaterialMappings.get(trueColor);
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
