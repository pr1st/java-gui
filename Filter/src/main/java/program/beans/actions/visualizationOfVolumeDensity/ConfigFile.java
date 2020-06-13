package program.beans.actions.visualizationOfVolumeDensity;


import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class ConfigFile {
    public class Absorption {
        public int x;
        public double value;

        Absorption(int x, double value) {
            this.x = x;
            this.value = value;
        }
    }

    public class Emission {
        public int x;
        public Color color;

        Emission(int x, int r, int g, int b) {
            this.x = x;
            this.color = new Color(r, g, b);
        }
    }

    public class Charge {
        public double x;
        public double y;
        public double z;
        public double value;

        Charge(double x, double y, double z, double value) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.value = value;
        }
    }

    public Absorption[] absorptions;
    public Emission[] emissions;
    public Charge[] charges;

    private Scanner scanner;

    public ConfigFile(Reader reader) throws IOException {
        scanner = new Scanner(reader);

        absorptions = new Absorption[getInteger()];

        for(int i = 0; i < absorptions.length; i++) {
            absorptions[i] = new Absorption(getInteger(), getDouble());
        }

        emissions = new Emission[getInteger()];

        for(int i = 0; i < emissions.length; i++) {
            emissions[i] = new Emission(getInteger(),getInteger(),getInteger(),getInteger());
        }

        charges = new Charge[getInteger()];

        for (int i = 0; i < charges.length; i++) {
            charges[i] = new Charge(getDouble(),getDouble(),getDouble(),getDouble());
        }
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
}
