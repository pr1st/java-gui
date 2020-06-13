package program.logic.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class AbstractImage extends BufferedImage {
    protected BiFunction<Double, Double, Double> f;

    AbstractImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
        this.f = f;
        Graphics2D g = createGraphics();
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, width, height);
    }
}
