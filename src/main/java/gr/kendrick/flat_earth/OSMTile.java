package gr.kendrick.flat_earth;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;

public class OSMTile {
    private final String tileServerURL;
    private final String userAgent;
    private final int zoomLevel;
    private final int x;
    private final int y;
    private BufferedImage image;

    private static final HashMap<String, BufferedImage> imageCache = new HashMap<>();
    private static final HashSet<String> failedDownloads = new HashSet<>();

    public OSMTile(String tileServerURLTemplate, String userAgent, int zoomLevel, int x, int y) {
        this.zoomLevel = zoomLevel;
        this.x = x;
        this.y = y;
        this.userAgent = userAgent;
        this.tileServerURL = tileServerURLTemplate.replace("${z}", Integer.toString(zoomLevel))
                .replace("${x}", Integer.toString(x))
                        .replace("${y}", Integer.toString(y));
        String tileId = this.getTileID();
        if (imageCache.containsKey(tileId)) {
            this.image = imageCache.get(tileId);
        } else if (failedDownloads.contains(tileId)) {
            this.image = null;
        } else {
            try {
                this.downloadImage();
            } catch (Exception e) {
                failedDownloads.add(tileId);
                e.printStackTrace();
            }
        }
    }

    private String getTileID() {
        return zoomLevel + "," + x + "," + y;
    }

    private void downloadImage() throws IOException, InterruptedException {
        // 1) --> https://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
        // 2) --> https://stackoverflow.com/questions/3360712/java-load-image-from-urlconnection
        URL url = new URL(this.tileServerURL); // see 1)
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", this.userAgent);
        Image img;
        try (BufferedInputStream in = new BufferedInputStream(con.getInputStream())) { // see 2)
            // Note: https://stackoverflow.com/questions/24362980/when-i-close-a-bufferedinputstream-is-the-underlying-inputstream-also-closed
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                byteArrayOut.write(c);
            }
            img = Toolkit.getDefaultToolkit().createImage(byteArrayOut.toByteArray());
        }
        // ----- Now wait until the Image is fully loaded: -----
        // -> https://stackoverflow.com/questions/4260500/how-to-wait-until-an-image-is-fully-loaded-in-java
        // -> the dirty 'while (img.getWidth(null) == -1) {Thread.onSpinWait();}' is not enough waiting!!!
        // -> https://docs.oracle.com/javase/6/docs/api/java/awt/MediaTracker.html
        MediaTracker mediaTracker = new MediaTracker(new java.awt.Canvas()); // (we cannot pass null as the argument!)
        mediaTracker.addImage(img, 0);
        mediaTracker.waitForID(0);
        this.image = toBufferedImage(img);
        imageCache.put(this.getTileID(), this.image);
    }

    /**
     * Copy-pasted from: https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public Color getColorRelativeToUpperLeftOrigin(int x, int y) {
        // "All BufferedImage objects have an upper left corner coordinate of (0, 0)."

        if (this.image == null) {
            return Color.BLACK; // return Color BLACK when no image is available (e.g., because of a download error)
        }

        int clr = this.image.getRGB(x, y);
        int red =   (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue =   clr & 0x000000ff;
        return new Color(red, green, blue);
    }

    public Color getColorRelativeToLowerLeftOrigin(int x, int y) {
        // Say the height of this image is 256, then:
        // height - (y=0) = 256
        // height - (y=256) = 0
        return this.getColorRelativeToUpperLeftOrigin(x, this.image.getHeight() - y);
    }

    public String getTileServerURL() {
        return this.tileServerURL;
    }

    public int getZoomLevel() {
        return this.zoomLevel;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
