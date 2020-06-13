package program.logic.configs;

import program.logic.Matrix4X4;
import program.logic.Point3D;
import program.logic.ProgramUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProgramConfig {
    public double a;
    public double b;
    public double c;
    public double d;

    public int n;
    public int m;
    public int k;

    public double zn;
    public double zf;
    public double sw;
    public double sh;


    public double getMaxRange() {
        double max = Double.MIN_VALUE;
        for(BSplineConfig bSpline: elements) {
            bSpline.reCalculateMaxRange();
            double maxX;
            if (bSpline.centerPointInWorld.getX() > 0) {
                maxX = bSpline.maxRange + bSpline.centerPointInWorld.getX();
            } else {
                maxX = bSpline.maxRange - bSpline.centerPointInWorld.getX();
            }
            double maxZ;
            if (bSpline.centerPointInWorld.getZ() > 0) {
                maxZ = bSpline.maxRange + bSpline.centerPointInWorld.getZ();
            } else {
                maxZ = bSpline.maxRange - bSpline.centerPointInWorld.getZ();
            }
            if (max < Math.max(maxX, maxZ)) {
                max = Math.max(maxX, maxZ);
            }
        }
        return max;
    }

    public Color backGround;

    public Matrix4X4 worldRotationMatrix;

    public List<BSplineConfig> elements = new ArrayList<>();
    public int selectedElementId;

    public void validateParameters() {
        if (a < 0.0 || a > b || b > 1.0)
            throw new IllegalArgumentException("0 <= a <= b <= 1");

        if (c < 0.0 || c > d || d > 2 * Math.PI)
            throw new IllegalArgumentException("0 <= c <= d <= 2 * pi");

        if (n < 1 || m < 1 || k < 1)
            throw new IllegalArgumentException("n >= 1, m >= 1, k >= 1");

        if (zn < 0 || zn >= zf)
            throw new IllegalArgumentException("zn >= 0, zn < zf");
        if (elements.size() == 0)
            throw new IllegalArgumentException("at scene need to be at least 1 element");

        if (!ProgramUtil.isRotationMatrix(worldRotationMatrix.getMatrix()))
            throw new IllegalArgumentException("Incorrect rotation matrix");
    }

    public ProgramConfig() {
        a = 0.0;
        b = 1.0;

        c = 0.0;
        d = 2 * Math.PI;

        n = 5;
        m = 5;
        k = 5;

        zn = 2;
        zf = 12;
        sw = 8;
        sh = 5;

        backGround = Color.WHITE;

        elements.add(new BSplineConfig());
        selectedElementId = -1;


        worldRotationMatrix = new Matrix4X4(new double[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        validateParameters();
    }

    private Scanner scanner;
    public ProgramConfig(Reader reader) throws IOException {
        scanner = new Scanner(reader);
        n = getInteger();
        m = getInteger();
        k = getInteger();
        a = getDouble();
        b = getDouble();
        c = getDouble();
        d = getDouble();
        zn = getDouble();
        zf = getDouble();
        sw = getDouble();
        sh = getDouble();


        worldRotationMatrix = getMatrix();

        int red = getInteger();
        int green = getInteger();
        int blue = getInteger();
        if (!ProgramUtil.isInColorRange(red, green, blue)) {
            throw new IllegalArgumentException("Incorrect color");
        }
        backGround = new Color(red, green, blue);

        int numOfElements = getInteger();
        elements = new ArrayList<>(numOfElements);
        for (int i = 0; i < numOfElements; i++) {
            BSplineConfig bSpline = new BSplineConfig();
            red = getInteger();
            green = getInteger();
            blue = getInteger();
            if (!ProgramUtil.isInColorRange(red, green, blue)) {
                throw new IllegalArgumentException("Incorrect color");
            }
            bSpline.color = new Color(red, green, blue);
            bSpline.centerPointInWorld = new Point3D(getDouble(), getDouble(), getDouble());
            bSpline.rotationMatrix = getMatrix();
            int numOfPoints = getInteger();
            bSpline.points = new ArrayList<>(numOfPoints);
            for (int j = 0; j < numOfPoints; j++) {
                bSpline.points.add(new Point2D.Double(getDouble(), getDouble()));
            }
            elements.add(bSpline);
        }
        selectedElementId = -1;

        while (scanner.hasNextLine()) {
            String str = scanner.nextLine().trim();
            if (!(str.startsWith("//") || str.equals("")))
                break;
        }
        if (scanner.hasNext())
            throw new IOException();

        validateParameters();
    }

    private int getInteger() throws IOException {
        if (scanner.hasNextInt())
            return scanner.nextInt();
        if (scanner.hasNext()) {
            if (scanner.next().startsWith("//")) {
                scanner.nextLine();
                return getInteger();
            }
        }
        throw new IOException();
    }

    private double getDouble() throws IOException {
        if (scanner.hasNextDouble())
            return scanner.nextDouble();
        if (scanner.hasNext()) {
            if (scanner.next().startsWith("//")) {
                scanner.nextLine();
                return getDouble();
            }
        }
        throw new IOException();
    }

    private Matrix4X4 getMatrix() throws IOException{
        double[][] matrix = new double[4][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = getDouble();
            }
        }
        matrix[0][3] = matrix[1][3] = matrix[2][3] = matrix[3][0] = matrix[3][1] = matrix[3][2] = 0;
        matrix[3][3] = 1;
        return new Matrix4X4(matrix);
    }

    public ProgramConfig getCopy() {
        validateParameters();
        ProgramConfig res = new ProgramConfig();

        res.a = a;
        res.b = b;
        res.c = c;
        res.d = d;


        res.n = n;
        res.m = m;
        res.k = k;

        res.zn = zn;
        res.zf = zf;
        res.sw = sw;
        res.sh = sh;

        res.backGround = backGround;

        res.elements = new ArrayList<>(elements.size());
        for (BSplineConfig b: elements) {
            res.elements.add(b.getCopy());
        }
        res.selectedElementId = selectedElementId;

        res.worldRotationMatrix = new Matrix4X4(ProgramUtil.deepCopyMatrix(worldRotationMatrix.getMatrix()));

        return res;
    }
}
