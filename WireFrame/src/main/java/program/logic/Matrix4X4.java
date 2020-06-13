package program.logic;

public class Matrix4X4 {

    private double[][] matrix;


    public Matrix4X4(double[][] m) {
        matrix = m;
    }

    public Matrix4X4 multiply(Matrix4X4 another) {
        return new Matrix4X4(ProgramUtil.multiplyMatrices(matrix, another.getMatrix()));
    }

    public Vector3 multiply(Vector3 vector) {
        double[][] temp = ProgramUtil.multiplyMatrices(matrix,
                new double[][] {
                        {vector.getX()},
                        {vector.getY()},
                        {vector.getZ()},
                        {1}
                });
        return new Vector3(new double[] {
                temp[0][0] / temp[3][0],
                temp[1][0] / temp[3][0],
                temp[2][0] / temp[3][0],
        });
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public Matrix4X4 plus(Matrix4X4 another) {
        double[][] res = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res[i][j] = matrix[i][j] + another.getMatrix()[i][j];
            }
        }

        return new Matrix4X4(res);
    }
}
