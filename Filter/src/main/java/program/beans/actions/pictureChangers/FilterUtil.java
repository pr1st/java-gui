package program.beans.actions.pictureChangers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

public class FilterUtil {


    public static BufferedImage getCopy(BufferedImage im) {
        return new BufferedImage(im.getColorModel(), im.copyData(im.getRaster().createCompatibleWritableRaster()), im.isAlphaPremultiplied(), null);
    }

    public static BufferedImage applyForEachPixel(BufferedImage image, Function<Color, Color> function) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = new Color(image.getRGB(x, y));

                result.setRGB(x, y, function.apply(pixel).getRGB());
            }
        }
        return result;
    }

    public static BufferedImage applyFilter(BufferedImage image, int[][] matrix, double scale, Function<Integer, Integer> funcAfterKernelApplied) {
        if (matrix.length % 2 == 0 || matrix[0].length % 2 == 0)
            throw new IllegalArgumentException();

        BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int sumR = 0;
                int sumG = 0;
                int sumB = 0;

                int center = (matrix.length / 2);

                for (int i = -center; i < center + 1; i++) {
                    for (int j = -center; j < center + 1; j++) {

                        Color initColor;
                        initColor = getColorOrDefaultBlack(image, x +j, y + i);

                        sumR += initColor.getRed() * matrix[i + center][j + center] * scale;
                        sumG += initColor.getGreen() * matrix[i + center][j + center] * scale;
                        sumB += initColor.getBlue() * matrix[i + center][j + center] * scale;
                    }
                }

                if (funcAfterKernelApplied == null)
                    funcAfterKernelApplied = (i) -> i;

                sumR = clampColor(funcAfterKernelApplied.apply(sumR));
                sumG = clampColor(funcAfterKernelApplied.apply(sumG));
                sumB = clampColor(funcAfterKernelApplied.apply(sumB));

                result.setRGB(x, y, new Color(sumR,sumG,sumB).getRGB());
            }
        }

        return result;
    }


    public static BufferedImage findEdgesFilter(BufferedImage image, int[][] xFilterMatrix, int[][] yFilterMatrix, int limit) {
        BufferedImage bwImage = ToBlackAndWhiteAction.toBlackAndWhiteImage(image);

        BufferedImage xPart = applyFilter(bwImage, xFilterMatrix, 1, null);
        BufferedImage yPart = applyFilter(bwImage, yFilterMatrix, 1, null);


        return applyEdgesFilter(xPart, yPart, limit);
    }

    // supporting function for edges filter
    private static BufferedImage applyEdgesFilter(BufferedImage xComponent, BufferedImage yComponent, int limit) {
        BufferedImage result = new BufferedImage(xComponent.getWidth(), xComponent.getHeight(), xComponent.getType());

        for (int y = 0; y < xComponent.getHeight(); y++) {
            for (int x = 0; x < xComponent.getWidth(); x++) {
                Color xP = new Color(xComponent.getRGB(x, y));
                Color yP = new Color(yComponent.getRGB(x, y));

                int r = (int)Math.sqrt(xP.getRed() * xP.getRed() + yP.getRed() * yP.getRed());
                int g = (int)Math.sqrt(xP.getGreen() * xP.getGreen() + yP.getGreen() * yP.getGreen());
                int b = (int)Math.sqrt(xP.getBlue() * xP.getBlue() + yP.getBlue() * yP.getBlue());

                if (r > limit || g > limit || b > limit)
                    result.setRGB(x, y, Color.WHITE.getRGB());
                else
                    result.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        return result;
    }



    public static Color getColorOrDefaultBlack(BufferedImage image, int x, int y) {
        if (x < 0 || x >= image.getWidth()
                || y < 0 || y >= image.getHeight())
            return Color.BLACK;

        return new Color(image.getRGB(x, y));
    }

    public static int clampColor(int val) {
        val = val < 255 ? val : 255;
        val = val > 0 ? val : 0;
        return val;
    }
}
