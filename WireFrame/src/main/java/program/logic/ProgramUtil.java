package program.logic;

import java.awt.*;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class ProgramUtil {
    public static double[][] multiplyEachElement(double[][] matrix, double value) {
        double[][] res = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res[i][j] = matrix[i][j] * value;
            }
        }
        return res;
    }


    public static double[][] multiplyMatrices(double[][] m1, double[][] m2) {
        if (m1 == null || m1.length == 0 || m1[0].length == 0 ||
                m2 == null || m2.length == 0 || m2[0].length == 0 ||
                m1[0].length != m2.length)
            throw new IllegalArgumentException();
        double[][] res = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                res[i][j] = 0;
                for (int n = 0; n < m2.length; n++)
                    res[i][j] += m1[i][n] * m2[n][j];
            }
        }
        return res;
    }

    public static double[][] deepCopyMatrix(double[][] input) {
        if (input == null)
            return null;
        double[][] result = new double[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }

    public static boolean isInRectangle(Point p, int width, int height) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    public static boolean isRotationMatrix(double[][] m) {
        boolean incorrectMatrix = Stream.of(m)
                .flatMapToDouble(DoubleStream::of)
                .anyMatch((e)-> e < -1 || e > 1);
        if (incorrectMatrix)
            return false;
        incorrectMatrix = DoubleStream.of(m[0][3], m[1][3], m[2][3], m[3][0], m[3][1], m[3][2])
                .anyMatch((e)-> e != 0);
        if (incorrectMatrix)
            return false;
        if (m[3][3] != 1)
            return false;
        return true;
    }

    public static boolean isInColorRange(int r, int g, int b) {
        return  (r <= 255 && g <= 255 && b <= 255
                && r >= 0 && g >= 0 && b >= 0);
    }
}
