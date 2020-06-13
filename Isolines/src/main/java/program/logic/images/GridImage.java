package program.logic.images;

import program.logic.Configuration;
import program.logic.ProgramUtil;

import java.awt.*;
import java.util.function.BiFunction;

public class GridImage extends AbstractImage {
    public GridImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, f);
    }

    public void drawImage(Configuration configuration) {
        Graphics2D g = createGraphics();
        g.setBackground(new Color(0,0,0,0));
        g.clearRect(0,0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        for (Double x: configuration.xGrid) {
            for (Double y: configuration.yGrid) {
                Point uv = ProgramUtil.toPixelValue(configuration, x, y, getWidth(), getHeight());
                g.drawLine(uv.x, 0, uv.x, getHeight());
                g.drawLine(0, uv.y, getWidth(), uv.y);
            }
        }
    }
}
