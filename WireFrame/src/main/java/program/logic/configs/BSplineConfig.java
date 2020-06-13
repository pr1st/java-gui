package program.logic.configs;

import program.logic.Matrix4X4;
import program.logic.Point3D;
import program.logic.ProgramUtil;
import program.logic.components.WorldComponent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static program.logic.ProgramUtil.multiplyEachElement;
import static program.logic.ProgramUtil.multiplyMatrices;

public class BSplineConfig {
    private static double[][] M = multiplyEachElement(new double[][]
            {
                    {-1, 3, -3, 1},
                    {3, -6, 3, 0},
                    {-3, 0, 3, 0},
                    {1, 4, 1, 0}
            }, (double)1/6);



    public List<Point2D> points;

    public Color color;

    public Point3D centerPointInWorld;
    public Matrix4X4 rotationMatrix;


    public double maxRange;

    public void validateParameters() {
        if (points.size() < 4)
            throw new IllegalArgumentException("Should be at least 4 points");

        if (color.equals(Color.WHITE))
            throw new IllegalArgumentException("You won't see spline with white color!");

        if (!ProgramUtil.isRotationMatrix(rotationMatrix.getMatrix()))
            throw new IllegalArgumentException("Incorrect rotation matrix");

    }

    public BSplineConfig() {

        points = new ArrayList<>();
//        points.add(new Point2D.Double(-3,2));
//        points.add(new Point2D.Double(-2.9,1.9));
//        points.add(new Point2D.Double(-2,0.3));
//        points.add(new Point2D.Double(0,0));
//        points.add(new Point2D.Double(1,1));
//        points.add(new Point2D.Double(2,1.5));
//        points.add(new Point2D.Double(3,1));
//        points.add(new Point2D.Double(4,0.2));
//        points.add(new Point2D.Double(4.1,0.1));

        points.add(new Point2D.Double(-50,-5));
        points.add(new Point2D.Double(-45,1));
        points.add(new Point2D.Double(-40,2));
        points.add(new Point2D.Double(-35,1.9));
        points.add(new Point2D.Double(-30,8));
        points.add(new Point2D.Double(-25,9));
        points.add(new Point2D.Double(-20,12));
        points.add(new Point2D.Double(-15,8));
        points.add(new Point2D.Double(-10,9));
        points.add(new Point2D.Double(-5,5));

        color = Color.BLUE;


        centerPointInWorld = new Point3D(0,0,0);
        rotationMatrix = new Matrix4X4(new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        reCalculateMaxRange();
        validateParameters();
    }

    public void reCalculateMaxRange() {
        double left = points.stream().mapToDouble(Point2D::getX).min().getAsDouble();
        double right = points.stream().mapToDouble(Point2D::getX).max().getAsDouble();

        double bot = points.stream().mapToDouble(Point2D::getY).min().getAsDouble();
        double top = points.stream().mapToDouble(Point2D::getY).max().getAsDouble();

        double xRange = Math.abs(left) > Math.abs(right) ? Math.abs(left) : Math.abs(right);
        double yRange = Math.abs(bot) > Math.abs(top) ? Math.abs(bot) : Math.abs(top);
        maxRange = Math.max(xRange, yRange);
    }




    public Point2D getFunctionValue(int k, double t) {
        double[][] Gx = new double[][] {
                {points.get(k-1).getX()},
                {points.get(k).getX()},
                {points.get(k+1).getX()},
                {points.get(k+2).getX()}
        };
        double[][] Gy = new double[][] {
                {points.get(k-1).getY()},
                {points.get(k).getY()},
                {points.get(k+1).getY()},
                {points.get(k+2).getY()}
        };
        double[][] T = new double[][] { {t*t*t, t*t, t, 1} };

        double x = multiplyMatrices(multiplyMatrices(T, M), Gx)[0][0];
        double y = multiplyMatrices(multiplyMatrices(T, M), Gy)[0][0];

        return new Point2D.Double(x, y);
    }

    public Map.Entry<Integer, Double> getKAndTFunction(double u) {
        double stepOfOneBSplineComponent = (double)1 / (points.size() - 3);
        return new Map.Entry<Integer, Double>() {
            @Override
            public Integer getKey() {
                // might be situation where calculates not right value due to using floating point mathematics
                if (points.size() - (int)Math.floor(u / stepOfOneBSplineComponent) - 3 == 0)
                    return (int)Math.floor(u / stepOfOneBSplineComponent);
                return (int)Math.floor(u / stepOfOneBSplineComponent) + 1;
            }

            @Override
            public Double getValue() {
                return (u - (getKey() - 1) * stepOfOneBSplineComponent) / stepOfOneBSplineComponent;
            }

            @Override
            public Double setValue(Double value) {
                return null;
            }
        };
    }


    public BSplineConfig getCopy() {
        validateParameters();
        BSplineConfig res = new BSplineConfig();

        res.points = new ArrayList<>(points.size());
        for (Point2D p: points) {
            res.points.add(new Point2D.Double(p.getX(), p.getY()));
        }

        res.color = new Color(color.getRGB());

        res.centerPointInWorld = new Point3D(centerPointInWorld.getX(), centerPointInWorld.getY(), centerPointInWorld.getZ());
        res.rotationMatrix = new Matrix4X4(ProgramUtil.deepCopyMatrix(rotationMatrix.getMatrix()));

        res.maxRange = maxRange;

        return res;
    }
}
