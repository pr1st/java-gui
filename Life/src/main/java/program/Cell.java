package program;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;


public class Cell {

    private boolean isAlive = false;

    private double impact = 0;
    public double getImpact() {
        return impact;
    }
    public void setImpact(double impact) {
        this.impact = impact;
    }

    private int centerX;
    private int centerY;

    private int offsetX;
    private int offsetY;

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setCenterPosition(int x, int y) {
        centerX = x;
        centerY = y;
    }

    public Point getCenterPosition() {
        return new Point(centerX, centerY);
    }

    public void setOffsets(int x, int y) {
        offsetX = x;
        offsetY = y;
    }


    public void paintBounds(Graphics g) {
        // "|"
        drawLine(g, centerX - offsetX,
                centerY + offsetY / 2,
                centerX - offsetX,
                centerY - offsetY / 2);

        // "/"
        drawLine(g, centerX - offsetX,
                centerY - offsetY / 2,
                centerX,
                centerY - offsetY);

        // "\"
        drawLine(g, centerX,
                centerY - offsetY,
                centerX + offsetX,
                centerY - offsetY / 2);

        // "|"
        drawLine(g, centerX + offsetX,
                centerY - offsetY / 2,
                centerX + offsetX,
                centerY + offsetY / 2);

        // "/"
        drawLine(g, centerX + offsetX,
                centerY + offsetY / 2,
                centerX,
                centerY + offsetY);

        // "\"
        drawLine(g, centerX,
                centerY + offsetY,
                centerX - offsetX,
                centerY + offsetY / 2);
    }



    private static void drawLine(Graphics g, int x0, int y0, int x1, int y1) {

        // for swap operation
        // in java primitive types a and b we can swap like this
        // a = swap.applyAsInt(b, b = a);
        // it works, explanation: https://stackoverflow.com/questions/1363186/is-it-possible-to-write-swap-method-in-java/16826296#16826296
        IntBinaryOperator swap = (itself, fake) -> itself;

        boolean changedXY = false; // indicates changes in x and y coordinates
        int t;
        int z;


        // if angle between Ox and line is greater than 45 degrees, algorithm is not working
        // so we changing x and y and indicate it in boolean changedXY
        if (Math.abs(x1 - x0) < Math.abs(y1 - y0)) {
            x1 = swap.applyAsInt(y1, y1 = x1);
            x0 = swap.applyAsInt(y0, y0 = x0);

            changedXY = true;
        }

        // algorithm is drawing from left to right, so swap points if needed
        if (x1 - x0 < 0) {
            x1 = swap.applyAsInt(x0, x0 = x1);
            y1 = swap.applyAsInt(y0, y0 = y1);
        }


        // Bresenham's line algorithm
        int deltax = Math.abs(x1 - x0);
        int deltay = Math.abs(y1 - y0);
        int error = 0;
        int deltaerr = deltay;
        int y = y0;
        int diry;
        if (y1 - y0 > 0)
            diry = 1;
        else if (y1 - y0 < 0)
            diry = -1;
        else
            diry = 0;
        for(int x = x0; x <= x1; x++) {
            if (changedXY)
                g.drawLine(y,x,y,x);
            else
                g.drawLine(x,y,x,y);
            error = error + deltaerr;
            if (2*error >= deltax) {
                y = y + diry;
                error = error - deltax;
            }
        }

    }

    public void drawImpact(Graphics2D g) {
        DecimalFormat formatter = new DecimalFormat("#.#");
        g.setColor(Color.RED);
        g.drawString(formatter.format(impact) , centerX - 6, centerY + 2);
    }
}
