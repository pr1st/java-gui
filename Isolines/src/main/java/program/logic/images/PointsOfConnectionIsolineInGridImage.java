package program.logic.images;

import java.awt.*;
import java.util.function.BiFunction;

class PointsOfConnectionIsolineInGridImage extends AbstractImage {
    PointsOfConnectionIsolineInGridImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, f);
    }

    private final int circleSize = 6;

    void drawSmallCircle(int x, int y) {
        Graphics2D g = createGraphics();
        g.setColor(Color.BLACK);
        int topLeftX = x - circleSize/2;
        int topLeftY = y - circleSize/2;
        g.drawOval(topLeftX, topLeftY, circleSize, circleSize);
    }
}
