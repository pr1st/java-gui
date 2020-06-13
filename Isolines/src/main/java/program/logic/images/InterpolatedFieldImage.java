package program.logic.images;

import program.logic.Configuration;
import program.logic.ProgramUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class InterpolatedFieldImage extends AbstractImage {
    public InterpolatedFieldImage(int width, int height, BiFunction<Double, Double, Double> f) {
        super(width, height, f);
    }

    public void drawImage(Configuration configuration) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Point2D fxy = ProgramUtil.toRealValue(configuration, x, y, getWidth(), getHeight());
                double z = f.apply(fxy.getX(), fxy.getY());

                int minIndex = ProgramUtil.getMaxIndexOfIsolineThatIsSmallerThanValue(configuration, z);

                double smallerZ, greaterZ;
                Color smallerColor, greaterColor;

                if (minIndex == -1) {
                    smallerZ = configuration.min;
                    smallerColor = configuration.areaColors.get(0);
                    greaterZ = configuration.isolineValues.get(0);
                    greaterColor = configuration.areaColors.get(1);
                } else if (minIndex == configuration.isolineValues.size() - 1) {
                    setRGB(x, y, configuration.areaColors.get(configuration.isolineValues.size()).getRGB());
                    continue;
                } else {
                    smallerZ = configuration.isolineValues.get(minIndex);
                    smallerColor = configuration.areaColors.get(minIndex + 1);
                    greaterZ = configuration.isolineValues.get(minIndex + 1);
                    greaterColor = configuration.areaColors.get(minIndex + 1 + 1);
                }



                BiFunction<Integer, Integer, Integer> interpolationForOneChannel = (c1, c2) -> {
                    return (int)(c1 * (greaterZ - z)/ (greaterZ - smallerZ) + c2 * (z - smallerZ) / (greaterZ - smallerZ));
                };


                Color res = new Color(
                        ProgramUtil.clampColor(interpolationForOneChannel.apply(smallerColor.getRed(), greaterColor.getRed())),
                        ProgramUtil.clampColor(interpolationForOneChannel.apply(smallerColor.getGreen(), greaterColor.getGreen())),
                        ProgramUtil.clampColor(interpolationForOneChannel.apply(smallerColor.getBlue(), greaterColor.getBlue()))
                );
                setRGB(x, y, res.getRGB());
            }
        }
    }
}
