package program.logic.images;

import program.logic.Configuration;
import program.logic.ProgramUtil;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class FieldImage extends AbstractImage {

    public FieldImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, f);
    }

    public void drawImage(Configuration configuration) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Point2D fxy = ProgramUtil.toRealValue(configuration, x, y, getWidth(), getHeight());
                double z = f.apply(fxy.getX(), fxy.getY());
                setRGB(x, y, ProgramUtil.colorOfRealValue(configuration, z).getRGB());
            }
        }
    }
}
