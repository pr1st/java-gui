package program.logic.images;

import program.MainFunction;
import program.logic.Configuration;
import program.logic.ProgramUtil;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class IsolineImage extends AbstractImage {

    private PointsOfConnectionIsolineInGridImage pointsImage;
    private Configuration configuration;
    private double z;

    public IsolineImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, f);
    }


    public void drawImage(Configuration configuration, double z) {
        this.configuration = configuration;
        this.z = z;
        pointsImage = new PointsOfConnectionIsolineInGridImage(getWidth(), getHeight(), MainFunction::f);

        List<Double> xGrid = configuration.xGrid;
        List<Double> yGrid = configuration.yGrid;

        Graphics2D g = createGraphics();
        g.setColor(configuration.isolineColor);

        for (int y = 0; y < yGrid.size() - 1; y++) {
            for (int x = 0; x < xGrid.size() - 1; x++) {
                double dx = xGrid.get(x + 1) - xGrid.get(x);
                double dy = yGrid.get(y + 1) - yGrid.get(y);

                double f1 = f.apply(xGrid.get(x), yGrid.get(y + 1));
                double f2 = f.apply(xGrid.get(x + 1), yGrid.get(y + 1));
                double f3 = f.apply(xGrid.get(x), yGrid.get(y));
                double f4 = f.apply(xGrid.get(x + 1), yGrid.get(y));

                Function<Double, Double> resolveEqualityProblem = (fi) -> (fi == z) ? fi + Math.ulp(fi) : fi;

                f1 = resolveEqualityProblem.apply(f1);
                f2 = resolveEqualityProblem.apply(f2);
                f3 = resolveEqualityProblem.apply(f3);
                f4 = resolveEqualityProblem.apply(f4);


                Double topX = getPlaceOfIsolineCoordinateOrNullIfNotFound(xGrid.get(x), dx, f1, f2);
                Double rightY = getPlaceOfIsolineCoordinateOrNullIfNotFound(yGrid.get(y), dy, f4, f2);
                Double botX = getPlaceOfIsolineCoordinateOrNullIfNotFound(xGrid.get(x), dx, f3, f4);
                Double leftY = getPlaceOfIsolineCoordinateOrNullIfNotFound(yGrid.get(y), dy, f3, f1);


                switch ((int) Stream.of(topX, rightY, botX, leftY).filter(Objects::nonNull).count()) {
                    case 0: {
                        break;
                    }
                    case 2: {
                        Point[] p = new Point[2];
                        int i = 0;
                        if (topX != null) {
                            p[i] = toPixel(topX, yGrid.get(y+1));
                            i++;
                        }
                        if (botX != null) {
                            p[i] = toPixel(botX, yGrid.get(y));
                            i++;
                        }
                        if (rightY != null) {
                            p[i] = toPixel(xGrid.get(x+1), rightY);
                            i++;
                        }
                        if (leftY != null) {
                            p[i] = toPixel(xGrid.get(x), leftY);
                        }

                        pointsImage.drawSmallCircle(p[0].x, p[0].y);
                        pointsImage.drawSmallCircle(p[1].x, p[1].y);
                        g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
                        break;
                    }
                    case 4: {
                        double center = (f1 + f2 + f3 + f4) / 4;
                        center = resolveEqualityProblem.apply(center);

                        // idea is wrong at this, as stream to this case can only be, when all values are not null
                        Point p1 = toPixel(xGrid.get(x), leftY);
                        Point p2 = toPixel(botX, yGrid.get(y));

                        Point p3 = toPixel(xGrid.get(x + 1), rightY);
                        Point p4 = toPixel(topX, yGrid.get(y + 1));


                        if (!(f1 > z && z < center || f1 < z && z > center)) {
                            Point t = p1;
                            p1 = p3;
                            p3 = t;
                        }

                        pointsImage.drawSmallCircle(p1.x, p1.y);
                        pointsImage.drawSmallCircle(p2.x, p2.y);
                        pointsImage.drawSmallCircle(p3.x, p3.y);
                        pointsImage.drawSmallCircle(p4.x, p4.y);
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        g.drawLine(p3.x, p3.y, p4.x, p4.y);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Your program is not working");
                }
            }
        }
    }


    private Point toPixel(double x, double y) {
        return ProgramUtil.toPixelValue(configuration, x, y, getWidth(),getHeight());
    }


    private Double getPlaceOfIsolineCoordinateOrNullIfNotFound(double x,double dx, double f1, double f2) {
        if ((z < f1 && z < f2) || (z > f1 && z> f2))
            return null;

        if (f1 < f2)
            return x + dx * (z - f1) / (f2 - f1);
        return x + dx * (1.0 - (z - f2) / (f1 - f2));
    }

    public PointsOfConnectionIsolineInGridImage getPointsImage() {
        return pointsImage;
    }
}
