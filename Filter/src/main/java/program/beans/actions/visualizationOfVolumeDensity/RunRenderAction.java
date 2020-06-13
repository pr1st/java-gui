package program.beans.actions.visualizationOfVolumeDensity;


import program.beans.actions.pictureChangers.FilterUtil;
import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.beans.utilBeans.Point3D;
import program.beans.utilBeans.factoryForDialogs.MessageDialogFactory;
import program.beans.utilBeans.factoryForDialogs.VolumeRenderingParamDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;


@SingletonBean
public class RunRenderAction extends AbstractAction {


    private ConfigFile configFile;

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;


    @Inject
    EnableAbsorptionAction enableAbsorptionAction;

    @Inject
    EnableEmissionAction enableEmissionAction;

    @Inject
    MessageDialogFactory messageDialogFactory;


    @Inject
    VolumeRenderingParamDialogFactory volumeRenderingParamDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (configFile == null) {
            messageDialogFactory.getDialog("Error", "Configuration is not set");
            return;
        }
        if (!enableAbsorptionAction.isActivated() && !enableEmissionAction.isActivated()) {
            messageDialogFactory.getDialog("Error", "Nothing to calculate, enable at least one action");
            return;
        }
        volumeRenderingParamDialogFactory.getDialog(this::calculateVolumeImage);
    }


    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }


    private int nX;
    private int nY;
    private int nZ;
    private boolean absorption;
    private boolean emission;
    private double min;
    private double max;

    public void calculateVolumeImage(Integer... params) {
        if (params.length != 3)
            throw new IllegalArgumentException();
        nX = params[0];
        nY = params[1];
        nZ = params[2];

        absorption = enableAbsorptionAction.isActivated();
        emission = enableEmissionAction.isActivated();

        double localMin = Double.MAX_VALUE;
        double localMax = Double.MIN_VALUE;

        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                for (int z = 0; z < nZ; z++) {
                    double val = sumAllCharges(new Point3D(x, y, z));
                    if (val > localMax)
                        localMax = val;
                    if (val < localMin)
                        localMin = val;
                }
            }
        }

        min = localMin;
        max = localMax;

        BufferedImage image = secondComponent.getImage();
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        int pixelsInVoxelY = image.getHeight() / nY + ((image.getHeight() % nY != 0) ? 1 : 0);
        int pixelsInVoxelX = image.getWidth() / nX + ((image.getWidth() % nX != 0) ? 1 : 0);


        double dz = 1.0 / nZ;

        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {

                for (int yInVoxel = 0; yInVoxel < pixelsInVoxelY; yInVoxel++) {
                    for (int xInVoxel = 0; xInVoxel < pixelsInVoxelX; xInVoxel++) {
                        int curX = pixelsInVoxelX * x + xInVoxel;
                        int curY = pixelsInVoxelY * y + yInVoxel;

                        if (curX < 0 || curX >= image.getWidth() || curY < 0 || curY >= image.getHeight())
                            continue;

                        Color initColor = new Color(image.getRGB(curX, curY));

                        double fogR = initColor.getRed();
                        double fogG = initColor.getGreen();
                        double fogB = initColor.getBlue();

                        for (int z = 0; z < nZ; z++) {
                            Color fogC = emissionValue(new Point3D(x, y, z));
                            double tauResult = absorptionValue(new Point3D(x, y, z));

                            fogR = fogR * Math.exp(-tauResult * dz) + fogC.getRed() * dz;
                            fogG = fogG * Math.exp(-tauResult * dz) + fogC.getGreen() * dz;
                            fogB = fogB * Math.exp(-tauResult * dz) + fogC.getBlue() * dz;
                        }


                        result.setRGB(curX, curY,
                                new Color(FilterUtil.clampColor((int)(fogR)),
                                        FilterUtil.clampColor((int)(fogG)),
                                        FilterUtil.clampColor((int)(fogB))
                                ).getRGB()
                        );
                    }
                }

            }
        }
        thirdComponent.setImage(result);
    }


    private double sumAllCharges(Point3D point) {
        double dz = 1.0 / nZ;
        double dy = 1.0 / nY;
        double dx = 1.0 / nX;

        double result = 0;

        for (ConfigFile.Charge c: configFile.charges) {
            double r = Math.sqrt(
                    Math.pow(point.getX() * dx - c.x, 2) +
                            Math.pow(point.getY() * dy - c.y, 2) +
                            Math.pow(point.getZ() * dz - c.z, 2)
            );
            r = (r > 0.1) ? r : 0.1;
            result += c.value / r;
        }
        return result;
    }

    private double toRange0to100(double value) {
        return (value - min) / (max - min) * 100;
    }

    private Color emissionValue(Point3D point) {
        if (!emission)
            return Color.BLACK;

        double x = toRange0to100(sumAllCharges(point));


        ConfigFile.Emission[] e = configFile.emissions;
        int i = 1;
        while (e[i].x < x) {
            i++;
        }

        if (e[i].color.equals(e[i - 1].color))
            return configFile.emissions[i].color;

        int red, green, blue;

        double oneStepValR = (double)(e[i].color.getRed() - e[i - 1].color.getRed()) / (e[i].x - e[i - 1].x);
        double oneStepValG = (double)(e[i].color.getGreen() - e[i - 1].color.getGreen()) / (e[i].x - e[i - 1].x);
        double oneStepValB = (double)(e[i].color.getBlue() - e[i - 1].color.getBlue()) / (e[i].x - e[i - 1].x);

        if (e[i].color.getRed() != e[i - 1].color.getRed())
            red = (int)(oneStepValR * x);
        else
            red = e[i].color.getRed();

        if (e[i].color.getGreen() != e[i - 1].color.getGreen())
            green = (int)(oneStepValG * x);
        else
            green = e[i].color.getGreen();

        if (e[i].color.getBlue() != e[i - 1].color.getBlue())
            blue = (int)(oneStepValB * x);
        else
            blue = e[i].color.getBlue();

        return new Color(red, green, blue);
    }

    private double absorptionValue(Point3D point) {
        if (!absorption)
            return 0;

        double x = toRange0to100(sumAllCharges(point));

        ConfigFile.Absorption[] abs = configFile.absorptions;
        int i = 1;
        while (abs[i].x < x) {
            i++;
        }
        if (abs[i].value == abs[i - 1].value)
            return abs[i].value;

        double oneStepVal = (abs[i].value - abs[i - 1].value) / (abs[i].x - abs[i - 1].x);
        return x * oneStepVal;
    }
}
