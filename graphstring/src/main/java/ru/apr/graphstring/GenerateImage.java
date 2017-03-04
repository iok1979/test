package ru.apr.graphstring;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class GenerateImage {

    public static InputStream getText(String text, int fsize, int w, int h, int degreed) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D cg = bufferedImage.createGraphics();
        RenderingHints rh = new RenderingHints(null);
        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        rh.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        //rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        cg.setRenderingHints(rh);

        Font font = new Font("Arial", Font.BOLD, fsize);
        cg.setColor(Color.LIGHT_GRAY);
        cg.setFont(font);

        switch (degreed) {
            case 0:
            case 90:
            case 180:
            case 270:
            case 360:
                cg.drawString(text, font.getSize() + 5, font.getSize() + 5);
                break;
            default:
                cg.drawString(text, w / 32, h / 2);

        }

        if (degreed != 0) {
            bufferedImage = GenerateImage.rotate(bufferedImage, degreed);
        }

        InputStream is = null;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            if (ImageIO.write(bufferedImage, "png", output)) {
                is = new ByteArrayInputStream(output.toByteArray());
            }
        }
        return is;
    }

    /**
     * Affline transform only works with perfect squares.
     *
     * The following code is used to take any rectangle image and rotate it correctly. To do this it
     * chooses a center point that is half the greater length and tricks the library to think the
     * image is a perfect square, then it does the rotation and tells the library where to find the
     * correct top left point. The special cases in each orientation happen when the extra image
     * that doesn't exist is either on the left or on top of the image being rotated. In both cases
     * the point is adjusted by the difference in the longer side and the shorter side to get the
     * point at the correct top left corner of the image. NOTE: the x and y axes also rotate with
     * the image so where width > height the adjustments always happen on the y axis and where the
     * height > width the adjustments happen on the x axis.
     *
     * @param image
     * @return
     */
    private static BufferedImage rotate(BufferedImage image, int thetaInDegrees) {

        AffineTransform xform = new AffineTransform();

        if (image.getWidth() > image.getHeight()) {
            xform.setToTranslation(0.5 * image.getWidth(), 0.5 * image.getWidth());
            xform.rotate(Math.toRadians(thetaInDegrees));

            int diff = image.getWidth() - image.getHeight();

            switch (thetaInDegrees) {
                case 90:
                    xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth() + diff);
                    break;
                case 180:
                    xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth() + diff);
                    break;
                default:
                    xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth());
                    break;
            }
        } else if (image.getHeight() > image.getWidth()) {
            xform.setToTranslation(0.5 * image.getHeight(), 0.5 * image.getHeight());
            xform.rotate(Math.toRadians(thetaInDegrees));

            int diff = image.getHeight() - image.getWidth();

            switch (thetaInDegrees) {
                case 180:
                    xform.translate(-0.5 * image.getHeight() + diff, -0.5 * image.getHeight());
                    break;
                case 270:
                    xform.translate(-0.5 * image.getHeight() + diff, -0.5 * image.getHeight());
                    break;
                default:
                    xform.translate(-0.5 * image.getHeight(), -0.5 * image.getHeight());
                    break;
            }
        } else {
            xform.setToTranslation(0.5 * image.getWidth(), 0.5 * image.getHeight());
            xform.rotate(Math.toRadians(thetaInDegrees));
            xform.translate(-0.5 * image.getHeight(), -0.5 * image.getWidth());
        }

        AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage newImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        return op.filter(image, newImage);
    }
}
