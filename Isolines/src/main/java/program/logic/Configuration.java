package program.logic;

import program.MainFunction;
import program.logic.components.MainField;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Configuration {
    public double a;
    public double b;
    public double c;
    public double d;

    public int n;
    public int k;
    public int m;

    public List<Double> isolineValues;
    public List<Color> areaColors;
    public Color isolineColor;

    public List<Double> xGrid;
    public List<Double> yGrid;

    public double max;
    public double min;

    private Configuration() {
    }

    public static Configuration getDefault() {
        Configuration con = new Configuration();
        con.a = -5;
        con.b = 5;
        con.c = -5;
        con.d = 5;

        con.n = 4;
        con.k = 7;
        con.m = 7;
        con.isolineValues = new ArrayList<>(con.n);

        con.areaColors = new ArrayList<>(con.n + 1);
        con.areaColors.add(Color.BLUE);
        con.areaColors.add(Color.GREEN);
        con.areaColors.add(Color.RED);
        con.areaColors.add(Color.YELLOW);
        con.areaColors.add(Color.WHITE);

        con.isolineColor = Color.BLACK;

        con.xGrid = new ArrayList<>(con.k);
        con.yGrid = new ArrayList<>(con.m);

        return con;
    }



    private Scanner scanner;
    public Configuration(Reader reader) throws IOException {
        scanner = new Scanner(reader);
        k = getInteger();
        if (k < 2)
            throw new IllegalArgumentException();
        m = getInteger();
        if (m < 2)
            throw new IllegalArgumentException();
        n = getInteger();
        if (n < 1)
            throw new IllegalArgumentException();

        isolineValues = new ArrayList<>(n);

        areaColors = new ArrayList<>(n +1);
        for (int i = 0; i < n+1; i++) {
            int r = getInteger();
            int g = getInteger();
            int b = getInteger();
            if (ProgramUtil.isNotInRangeOfColor(r,g,b))
                throw new IllegalArgumentException();
            areaColors.add(new Color(r,g,b));
        }

        int r = getInteger();
        int g = getInteger();
        int bl = getInteger();
        if (ProgramUtil.isNotInRangeOfColor(r,g,bl))
            throw new IllegalArgumentException();
        isolineColor = new Color(r, g, bl);


        while (scanner.hasNextLine()) {
            String str = scanner.nextLine().trim();
            if (!(str.startsWith("//") || str.equals("")))
                break;
        }
        if (scanner.hasNext())
            throw new IOException();

        xGrid = new ArrayList<>(k);
        yGrid = new ArrayList<>(m);

        a = MainField.instance().getConfiguration().a;
        b = MainField.instance().getConfiguration().b;
        c = MainField.instance().getConfiguration().c;
        d = MainField.instance().getConfiguration().d;
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





    public void setIsolineValues(int xPixels, int yPixels) {
        isolineValues.clear();
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;

        for (int y = 0; y < yPixels; y++) {
            for (int x = 0; x < xPixels; x++) {
                Point2D fxy = ProgramUtil.toRealValue(this, x, y, xPixels, yPixels);
                double z = MainFunction.f(fxy.getX(), fxy.getY());
                max = (z > max) ? z : max;
                min = (z < min) ? z : min;
            }
        }

        double step = (max - min) / (n + 1);
        for (int i = 1; i <= n; i++) {
            isolineValues.add(i * step + min);
        }
        setGridValues();
    }

    private void setGridValues() {
        xGrid.clear();
        yGrid.clear();
        double xStep = (b - a) / (k - 1);
        for (int i = 0; i < k; i++) {
            xGrid.add(i * xStep + a);
        }

        double yStep = (d - c) / (m - 1);
        for (int i = 0; i < m; i++) {
            yGrid.add(i * yStep + c);
        }
    }
}
