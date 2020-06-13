package program.logic.images;

import program.logic.configs.BSplineConfig;
import program.logic.components.WorldComponent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class BSplineImage extends BufferedImage {
    private static int pointsCircleSizeInPixels = 6;


    private BSplineConfig config;

    public BSplineImage(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = createGraphics();
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, width, height);
    }


    public void setConfig(BSplineConfig config) {
        this.config = config;
        drawImage();
    }

    private void drawImage() {
        drawAxis();

        double stepOfOneBSplineComponent = (double)1 / (config.points.size() - 3);

        Graphics2D g = createGraphics();
        Point pixelBefore = null;
        for (int i = 1; i < config.points.size() - 2; i++) {
            for (int j = 0; j < WorldComponent.instance().getConfig().k; j++) {
                double t;
                if (WorldComponent.instance().getConfig().k != 1)
                    t = (double)j / (WorldComponent.instance().getConfig().k - 1);
                else
                    t = 0;

                Point2D funcValue = config.getFunctionValue(i, t);

                if (stepOfOneBSplineComponent * (i - 1 + t) < WorldComponent.instance().getConfig().a
                        || stepOfOneBSplineComponent * (i - 1 + t) > WorldComponent.instance().getConfig().b) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(config.color);
                }

                Point pixel = toPixel(funcValue.getX(), funcValue.getY());
                if (pixelBefore != null) {
                    g.drawLine(pixelBefore.x, pixelBefore.y, pixel.x, pixel.y);
                }
                pixelBefore = pixel;
            }
        }

        for (Point2D point: config.points) {
            Point pixel = toPixel(point.getX(), point.getY());
            drawSmallCircle(pixel.x, pixel.y);
        }
    }

    private void drawSmallCircle(int x, int y) {
        Graphics2D g = createGraphics();
        g.setColor(Color.MAGENTA);
        int topLeftX = x - pointsCircleSizeInPixels/2;
        int topLeftY = y - pointsCircleSizeInPixels/2;
        g.drawOval(topLeftX, topLeftY, pointsCircleSizeInPixels, pointsCircleSizeInPixels);
    }

    private void drawAxis() {
        Graphics2D g = createGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(0, getHeight()/2,getWidth() - 1, getHeight()/2);
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() - 1);
        g.drawString("0",getWidth()/2 - 8, getHeight()/2 + 12);
        // adding 0.0001 to remove dashes at the end of axis in situations where maxRange is integer
        for (int x = -(int)((config.maxRange+1) + 0.0001); x < (config.maxRange+1); x++) {
            Point pixel = toPixel(x,0);
            g.drawLine(pixel.x, pixel.y - 2, pixel.x, pixel.y + 2);
        }
        for (int y = -(int)((config.maxRange+1) - 0.0001); y < (config.maxRange+1); y++) {
            Point pixel = toPixel(0,y);
            g.drawLine(pixel.x - 2, pixel.y, pixel.x + 2, pixel.y);
        }
    }


    public Point2D getSupportingPoint(int pixelX, int pixelY) {
        for (Point2D point: config.points) {
            Point pointInPixel= toPixel(point.getX(), point.getY());
            if (pixelX <= pointInPixel.x + pointsCircleSizeInPixels / 2 && pixelX >= pointInPixel.x - pointsCircleSizeInPixels / 2
                    && pixelY <= pointInPixel.y + pointsCircleSizeInPixels / 2 && pixelY >= pointInPixel.y - pointsCircleSizeInPixels / 2)
                return point;
        }
        return null;
    }

    public Point2D toRealValue(int pixelX, int pixelY) {
        double x = (2 * (config.maxRange+1)) * (double)(pixelX) / (getWidth() - 1) - (config.maxRange+1);
        double y = (-2 * (config.maxRange+1)) * (double)(pixelY) / (getHeight() - 1) + (config.maxRange+1);
        return new Point2D.Double(x, y);
    }

    private Point toPixel(double x, double y) {
        int pixelX = (int)((getWidth() - 1) * (x + (config.maxRange+1)) / (2 * (config.maxRange+1)) + 0.5);
        int pixelY = (int)((getHeight() - 1) * (y - (config.maxRange+1)) / (-2 * (config.maxRange+1)) + 0.5);
        return new Point(pixelX, pixelY);
    }
}
