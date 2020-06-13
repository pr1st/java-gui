package program.logic;

public class Vector3 {
    private double[] vector;

    public Vector3(double[] v) {
        vector = v;
    }

    public Vector3(Point3D p1, Point3D p2) {
        vector = new double[3];
        vector[0] = p2.getX() - p1.getX();
        vector[1] = p2.getY() - p1.getY();
        vector[2] = p2.getZ() - p1.getZ();

    }

    public Vector3(double x, double y, double z) {
        vector = new double[3];
        vector[0] = x;
        vector[1] = y;
        vector[2] = z;
    }


    public Vector3 plus(Vector3 another) {
        return new Vector3(
                getX() + another.getX(),
                getY() + another.getY(),
                getZ() + another.getZ()
        );
    }

    public Vector3 multiply(Vector3 another) {
        return new Vector3(
                getY() * another.getZ() - another.getY() * getZ(),
                getZ() * another.getX() - another.getZ() * getX(),
                getX() * another.getY() - another.getX() * getY()
        );
    }

    public Vector3 getNormalized() {

        double size = Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ());

        return new Vector3(
                getX() / size,
                getY() / size,
                getZ() / size
        );
    }

    public Vector3 multiply(Matrix4X4 matrix4X4) {
        double[][] res = ProgramUtil.multiplyMatrices(
                matrix4X4.getMatrix(),
                new double[][] {
                        {getX()},
                        {getY()},
                        {getZ()},
                        {1}
                });
        return new Vector3(res[0][0] / res[3][0], res[1][0] / res[3][0], res[2][0] / res[3][0]);
    }


    public double getX() {
        return vector[0];
    }

    public double getY() {
        return vector[1];
    }

    public double getZ() {
        return vector[2];
    }

    @Override
    public String toString() {
        return "Vector: " + getX() + " " + getY() + " " + getZ();
    }
}
