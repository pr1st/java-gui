package program;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Config {

    public int rows;
    public int columns;
    public int boundWidth;
    public int edgeLength;



    public double liveBegin;
    public double liveEnd;

    public double birthBegin;
    public double birthEnd;



    public double firstImpact;
    public double secondImpact;

    public boolean xorOption;

    public List<Point> coloredCells;


    public Config() {
        rows = 10;
        columns = 15;
        boundWidth = 6;
        edgeLength = 25;

        liveBegin = 2.0;
        liveEnd = 3.3;

        birthBegin = 2.3;
        birthEnd = 2.9;

        firstImpact = 1.0;
        secondImpact = 0.3;

        xorOption = false;
        coloredCells = new ArrayList<>();

    }


    public Config(Reader reader) throws IOException {
        this();
        Scanner scanner = new Scanner(reader);
        columns = getInteger(scanner);
        rows = getInteger(scanner);
        boundWidth = getInteger(scanner);
        edgeLength = getInteger(scanner);

        int size = getInteger(scanner);
        coloredCells = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            coloredCells.add(new Point(getInteger(scanner), getInteger(scanner)));
        }
    }

    private int getInteger(Scanner scanner) throws IOException {
        if (scanner.hasNextInt())
            return scanner.nextInt();
        if (scanner.hasNext()) {
            if (scanner.next().startsWith("//")) {
                scanner.nextLine();
                return getInteger(scanner);
            }
        }
        throw new IOException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(columns + " " + rows + "\n");
        builder.append(boundWidth + "\n");
        builder.append(edgeLength + "\n");
        builder.append(coloredCells.size() + "\n");

        for (int i = 0; i < coloredCells.size(); i++) {
            builder.append(coloredCells.get(i).x + " " + coloredCells.get(i).y + "\n");
        }

        return builder.toString();
    }

    public void addCoordinates(int x, int y) {
        coloredCells.add(new Point(x, y));
    }

    public void removeCoordinates(int x, int y) {
        for (Point point: coloredCells) {
            if (point.x == x && point.y == y) {
                coloredCells.remove(point);
                return;
            }
        }
        System.out.println(":((((((");
    }

    public void removeColumn(int x) {
        List<Point> bucket = new ArrayList<>();
        for(Point cell: coloredCells) {
            if(cell.x == x)
                bucket.add(cell);
        }
        for (Point cell: bucket) {
            coloredCells.remove(cell);
        }
    }

    public void removeRow(int y) {
        List<Point> bucket = new ArrayList<>();
        for(Point cell: coloredCells) {
            if(cell.y == y)
                bucket.add(cell);
        }
        for (Point cell: bucket) {
            coloredCells.remove(cell);
        }
    }


    public int getInPixelsInnerRadius() {
        return new Double((double)edgeLength * Math.cos(Math.toRadians(30))).intValue() + boundWidth / 2;
    }

    public int getInPixelsOuterRadius() {
        return edgeLength + boundWidth/2;
    }

    public int getInPixelsFieldHeight() {
        if (rows == 1)
            return getInPixelsOuterRadius() * 2 + boundWidth / 2 + 5;
        return (getInPixelsOuterRadius() * 7  / 2 + boundWidth / 2) * rows / 2 + 5;
    }

    public int getInPixelsFieldWidth() {
        return (getInPixelsInnerRadius() * 2 + boundWidth / 2) * columns;
    }
}
