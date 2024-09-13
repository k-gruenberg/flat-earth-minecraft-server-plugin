package gr.kendrick.flat_earth;

import org.bukkit.Material;

import java.awt.*;
import java.util.HashMap;

public class ColorToMaterialMappings {
    public static final String DEFAULT =
            "#000000=BLACK_TERRACOTTA," +
            "#ffffff=WHITE_TERRACOTTA," +
            "#808080=GRAY_TERRACOTTA," + // Color.GRAY ==> java.awt.Color[r=128,g=128,b=128]
            "#ff0000=RED_TERRACOTTA," +
            "#90ee90=LIME_TERRACOTTA," + // CSS "LightGreen"
            "#006400=GREEN_TERRACOTTA," + // CSS "DarkGreen"
            "#add8e6=LIGHT_BLUE_TERRACOTTA," + // CSS "LightBlue"
            "#00008b=BLUE_TERRACOTTA," + // CSS "DarkBlue"
            "#ffafaf=PINK_TERRACOTTA," + // Color.PINK ==> java.awt.Color[r=255,g=175,b=175]
            "#00ffff=CYAN_TERRACOTTA," + // Color.CYAN ==> java.awt.Color[r=0,g=255,b=255]
            "#ffc800=ORANGE_TERRACOTTA," + // Color.ORANGE ==> java.awt.Color[r=255,g=200,b=0]
            "#ffff00=YELLOW_TERRACOTTA"; // Color.YELLOW ==> java.awt.Color[r=255,g=255,b=0]

    private final HashMap<Color, Material> mappings;

    public ColorToMaterialMappings(String configString) {
        this.mappings = new HashMap<>();

        String[] mappingsAsStrings = configString.split(",");
        for (String mappingAsString : mappingsAsStrings) {
            String[] colorToMaterialMapping = mappingAsString.split("=");
            String colorAsString = colorToMaterialMapping[0];
            String materialAsString = colorToMaterialMapping[1];

            Color color = Color.decode(colorAsString); // e.g., Color.decode("#ff0000") => java.awt.Color[r=255,g=0,b=0]
            Material material = Material.getMaterial(materialAsString);

            this.mappings.put(color, material);
        }
    }

    public Material get(Color trueColor) {
        Color closestColor = this.closestAvailableColor(trueColor);
        return this.mappings.get(closestColor);
    }

    public Color closestAvailableColor(Color trueColor) {
        Color closestColor = null;
        for (Color color : this.mappings.keySet()) {
            if (colorDistance(trueColor, color) < colorDistance(trueColor, closestColor)) {
                closestColor = color; // update current knowledge of closestColor to trueColor
            }
        }
        return closestColor;
    }

    public static double colorDistance(Color color1, Color color2) {
        if (color1 == null || color2 == null) {
            return Integer.MAX_VALUE;
        }
        return Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2);
    }
}
