package program.logic.images;

import program.logic.*;
import program.logic.components.WorldComponent;
import program.logic.configs.BSplineConfig;
import program.logic.configs.ProgramConfig;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.stream.Stream;

import static program.logic.ProgramUtil.*;


public class WorldImage extends BufferedImage {

    private ProgramConfig config;

    public WorldImage(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = createGraphics();
        g.setBackground(WorldComponent.instance().getConfig().backGround);
        g.clearRect(0, 0, width, height);
    }

    public void setConfig(ProgramConfig config) {
        this.config = config;
        drawImage();
    }


    private Matrix4X4 toWorld;
    private Matrix4X4 toWorleScale;
    private Matrix4X4 toCamMove;
    private Matrix4X4 toCamRotate;
    private Matrix4X4 proj;

    private double maxRange;
    private void drawImage() {
        Graphics2D g = createGraphics();
        g.setBackground(config.backGround);
        g.clearRect(0,0, getWidth(),getHeight());
        maxRange = config.getMaxRange();


        for (BSplineConfig bSpline: config.elements) {
            configMatrices(bSpline);
            Matrix4X4 matrix = proj
                    .multiply(toCamRotate)
                    .multiply(toCamMove)
                    .multiply(toWorleScale)
                    .multiply(config.worldRotationMatrix)
                    .multiply(toWorld)
                    .multiply(bSpline.rotationMatrix);

            g.setColor(bSpline.color);
            if (config.selectedElementId != -1) {
                if (bSpline.equals(config.elements.get(config.selectedElementId)))
                    g.setStroke(new BasicStroke(2));
                else
                    g.setStroke(new BasicStroke(1));
            }
            for (int i = 0; i < config.n + 1; i++) {
                prevCamPoint = null;
                for (int j = 0; j < config.m + 1; j++) {
                    for(int k = 0; k < config.k; k++) {
                        double di = (double) i / config.n;
                        double dj = (double) j / config.m + (double)k * 1/config.m/config.k;
                        Point3D p = calculatePointByDiDj(bSpline, di, dj);

                        drawPoint(g, p, matrix);
                    }
                }
            }
            for (int j = 0; j < config.m; j++) {
                prevCamPoint = null;
                for (int i = 0; i < config.n + 1; i++) {
                    for(int k = 0; k < config.k; k++) {
                        double di = (double) i / config.n + (double)k * 1/config.n/config.k;
                        double dj = (double) j / config.m;
                        Point3D p = calculatePointByDiDj(bSpline, di, dj);

                        drawPoint(g, p, matrix);
                        if (i == config.n)
                            break;
                    }
                }
            }
            drawAxis(g, bSpline);
        }

        drawAxis(g, null);
        drawCube(g);

        g.setColor(Color.YELLOW);
        g.drawRect(0,0, getWidth() - 1, getHeight() - 1);
    }

    private Point3D calculatePointByDiDj(BSplineConfig bSpline, double di, double dj) {
        double u = config.a * (1 - di) + config.b * di;
        double v = config.c * (1 - dj) + config.d * dj;

        Map.Entry<Integer, Double> kt = bSpline.getKAndTFunction(u);
        Point2D value = bSpline.getFunctionValue(kt.getKey(), kt.getValue());
        return new Point3D(
                value.getY() * Math.cos(v),
                value.getY() * Math.sin(v),
                value.getX()
        );
    }

    private Point prevCamPoint = null;
    private Vector3 prevWorldPoint = null;
    private void drawPoint(Graphics2D g, Point3D localPoint, Matrix4X4 fromPointInWorldToCam) {
        Vector3 vecRes = fromPointInWorldToCam
                .multiply(new Vector3(new Point3D(0,0,0), localPoint));

        Point camPoint = new Point(
                (int)((vecRes.getX() + 1) / 2 * getWidth()),
                (int)(getHeight() - (vecRes.getY() + 1) / 2 * getHeight()));


        if (prevCamPoint != null) {
            boolean drawPrev =
                    isInRectangle(prevCamPoint, getWidth(), getHeight())
                            && prevWorldPoint.getZ() > 0
                            && prevWorldPoint.getZ() < 1;
            boolean drawCurrent =
                    isInRectangle(camPoint, getWidth(), getHeight())
                            && vecRes.getZ() > 0
                            && vecRes.getZ() < 1;

            if (drawPrev && drawCurrent) {
                g.drawLine(prevCamPoint.x, prevCamPoint.y, camPoint.x, camPoint.y);
            } else if (drawPrev) {
                drawOutOfRangeLine(g, prevCamPoint, camPoint);
            } else if (drawCurrent){
                drawOutOfRangeLine(g, camPoint, prevCamPoint);
            }
        }
        prevCamPoint = camPoint;
        prevWorldPoint = vecRes;
    }


    private void drawOutOfRangeLine(Graphics2D g, Point inRange, Point outOfRange) {

        Point p1 = clipBoundX(inRange, outOfRange);
        Point p2 = clipBoundY(inRange, p1);
        g.drawLine(inRange.x, inRange.y, p2.x, p2.y);
    }

    private Point clipBoundX(Point inRange, Point outOfRange) {
        int newX;
        int newY;
        if (outOfRange.x >= getWidth()) {
            newX = getWidth() - 1;
        } else if (outOfRange.x < 0) {
            newX = 0;
        } else {
            return outOfRange;
        }
        newY = inRange.y + (int)((outOfRange.y - inRange.y) * (double)(newX - inRange.x) / (double)(outOfRange.x -  inRange.x));
        return new Point(newX, newY);
    }

    private Point clipBoundY(Point inRange, Point outOfRange) {
        int newX;
        int newY;
        if (outOfRange.y >= getHeight()) {
            newY = getHeight() - 1;
        } else if (outOfRange.y < 0) {
            newY = 0;
        } else {
            return outOfRange;
        }
        newX = inRange.x + (int)((outOfRange.x - inRange.x) * (double)(newY - inRange.y) / (double)(outOfRange.y -  inRange.y));
        return new Point(newX, newY);
    }


    private void configMatrices(BSplineConfig bSpline) {
        toWorld = new Matrix4X4(new double[][] {
                {1, 0, 0, -bSpline.centerPointInWorld.getX()},
                {0, 1, 0, -bSpline.centerPointInWorld.getY()},
                {0, 0, 1, -bSpline.centerPointInWorld.getZ()},
                {0, 0, 0, 1}
        });
        toWorleScale = new Matrix4X4(new double[][] {
                {1 / maxRange, 0, 0, 0},
                {0, 1 / maxRange, 0, 0},
                {0, 0, 1 / maxRange, 0},
                {0, 0, 0, 1}
        });


        Point3D pRef = new Point3D(10, 0, 0);
        Point3D pEye = new Point3D(-10, 0, 0);
        Vector3 vecUp = new Vector3(0, 1, 0);

        Vector3 vecW = new Vector3(pEye, pRef).getNormalized();
        Vector3 vecU = vecUp.multiply(vecW).getNormalized();
        Vector3 vecV = vecW.multiply(vecU);


        toCamMove= new Matrix4X4(new double[][] {
                {1, 0, 0, -pEye.getX()},
                {0, 1, 0, -pEye.getY()},
                {0, 0, 1, -pEye.getZ()},
                {0, 0, 0, 1}
        });
        toCamRotate = new Matrix4X4(new double[][] {
                {vecU.getX(), vecU.getY(), vecU.getZ(), 0},
                {vecV.getX(), vecV.getY(), vecV.getZ(), 0},
                {vecW.getX(), vecW.getY(), vecW.getZ(), 0},
                {0, 0, 0, 1}
        });

        proj = new Matrix4X4(new double[][] {
                {2 / config.sw * config.zn, 0, 0, 0},
                {0, 2/ config.sh * config.zn, 0, 0},
                {0, 0, config.zf/(config.zf - config.zn), -config.zf * config.zn / (config.zf - config.zn)},
                {0, 0, 1, 0}
        });
    }


    private void drawAxis(Graphics2D g, BSplineConfig bSpline) {
        Matrix4X4 matrix = proj
                .multiply(toCamRotate)
                .multiply(toCamMove)
                .multiply(toWorleScale)
                .multiply(config.worldRotationMatrix);
        if (bSpline != null) {
            configMatrices(bSpline);
            matrix = matrix
                    .multiply(toWorld)
                    .multiply(bSpline.rotationMatrix);
        }

        g.setColor(Color.BLACK);
        if (bSpline != null)
            g.setStroke(new BasicStroke(1));
        else
            g.setStroke(new BasicStroke(2));

        prevCamPoint = null;
        drawPoint(g, new Point3D(0,0,0), matrix);
        drawPoint(g, new Point3D(0,0,maxRange), matrix);

        drawString(g, "z", new Vector3(0.1,0.1,maxRange), matrix);

        prevCamPoint = null;
        drawPoint(g, new Point3D(0,0,0), matrix);
        drawPoint(g, new Point3D(0,maxRange,0), matrix);

        drawString(g, "y", new Vector3(0.1,maxRange,0.1), matrix);

        prevCamPoint = null;
        drawPoint(g, new Point3D(0,0,0), matrix);
        drawPoint(g, new Point3D(maxRange,0,0), matrix);

        drawString(g, "x", new Vector3(maxRange,0.1,0.1), matrix);
    }

    private void drawString(Graphics2D g, String s, Vector3 point, Matrix4X4 matrix) {
        Vector3 vecRes = matrix
                .multiply(point);
        Point camPoint = new Point(
                (int)((vecRes.getX() + 1) / 2 * getWidth()),
                (int)(getHeight() - (vecRes.getY() + 1) / 2 * getHeight()));

        if (vecRes.getZ() > 0 && vecRes.getZ() < 1)
            g.drawString(s, camPoint.x, camPoint.y);
    }

    private void drawCube(Graphics2D g) {
        Matrix4X4 matrix = proj
                .multiply(toCamRotate)
                .multiply(toCamMove)
                .multiply(config.worldRotationMatrix);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));


        supportFunctionForDrawingCube(g, matrix, -1,-1,-1);
        supportFunctionForDrawingCube(g, matrix, -1,1,1);
        supportFunctionForDrawingCube(g, matrix, 1,-1,1);
        supportFunctionForDrawingCube(g, matrix, 1,1,-1);

    }

    private void supportFunctionForDrawingCube(Graphics2D g, Matrix4X4 matrix, int x, int y, int z) {
        prevCamPoint = null;
        drawPoint(g, new Point3D(x,y,z), matrix);
        drawPoint(g, new Point3D(-x,y,z), matrix);
        prevCamPoint = null;
        drawPoint(g, new Point3D(x,y,z), matrix);
        drawPoint(g, new Point3D(x,-y,z), matrix);
        prevCamPoint = null;
        drawPoint(g, new Point3D(x,y,z), matrix);
        drawPoint(g, new Point3D(x,y,-z), matrix);
    }
}
