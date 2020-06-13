package program.logic;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

public class ProgramUtil {
    public static Point2D toRealValue(Configuration c, int pixelX, int pixelY, int maxPixelsX, int maxPixelsY) {
        double x = (c.b - c.a) * (double)(pixelX) / (maxPixelsX - 1) + c.a;
        double y = -(c.d - c.c) * (double)(pixelY) / (maxPixelsY - 1) + c.d;
        return new Point2D.Double(x, y);
    }

    public static Point toPixelValue(Configuration c, double x, double y, int maxPixelsX, int maxPixelsY) {
        int pixelX = (int)((maxPixelsX - 1) * (x - c.a) / (c.b - c.a) + 0 + 0.5);
        int pixelY = maxPixelsY - (int)((maxPixelsY - 1) * (y - c.c) / (c.d - c.c) + 0 + 1.5);
        return new Point(pixelX, pixelY);
    }

    public static int getMaxIndexOfIsolineThatIsSmallerThanValue(Configuration c, double z) {
        List<Double> isolines = c.isolineValues;
        if (z >= isolines.get(isolines.size() - 1))
            return isolines.size() - 1;
        int i = 0;
        while (z >= isolines.get(i)) {
            i++;
        }
        return i - 1;
    }

    public static Color colorOfRealValue(Configuration c, double z) {
        return c.areaColors.get(getMaxIndexOfIsolineThatIsSmallerThanValue(c, z) + 1);
    }

    public static int clampColor(int val) {
        val = val < 255 ? val : 255;
        val = val > 0 ? val : 0;
        return val;
    }

    static boolean isNotInRangeOfColor(int r, int g, int b) {
        return IntStream.of(r, g, b).anyMatch((c)-> c < 0 || c > 255);
    }
}
