package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.beans.utilBeans.factoryForDialogs.FloydDitheringParamDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

@SingletonBean
public class FloydDitheringAction extends AbstractAction {

    private class ThreeValueErrorCLass {
        double r;
        double g;
        double b;

        void plusValues(double r, double g, double b, double scale) {
            this.r += scale * r;
            this.g += scale * g;
            this.b += scale * b;
        }
    }


    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Inject
    FloydDitheringParamDialogFactory floydDitheringParamDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        floydDitheringParamDialogFactory.getDialog();
    }

    public void performDither(int r, int g, int b) {
        thirdComponent.setImage(performFloydDither(secondComponent.getImage(), r, g, b));
    }

    public static BufferedImage performFloydDither(BufferedImage image, int r, int g, int b) {

        class ThreeValueErrorCLass {
            double r;
            double g;
            double b;

            void plusValues(double r, double g, double b, double scale) {
                this.r += scale * r;
                this.g += scale * g;
                this.b += scale * b;
            }
        }


        r = r - 1;
        g = g - 1;
        b = b - 1;

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        ThreeValueErrorCLass[][] error = new ThreeValueErrorCLass[image.getHeight()][image.getWidth()];

        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                error[i][j] = new ThreeValueErrorCLass();


        for (int y = 0; y < image.getHeight(); y ++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color initial = new Color(image.getRGB(x, y));


                double initialWithErrorRed = Math.rint((initial.getRed() + error[y][x].r));
                double initialWithErrorGreen = Math.rint((initial.getGreen() + error[y][x].g));
                double initialWithErrorBlue = Math.rint((initial.getBlue() + error[y][x].b));


                int changedRed = (int)Math.rint(initialWithErrorRed * r / 255) * 255 / r;
                changedRed = FilterUtil.clampColor(changedRed);

                int changedGreen = (int)Math.rint(initialWithErrorGreen * g / 255) * 255 / g;
                changedGreen = FilterUtil.clampColor(changedGreen);

                int changedBlue = (int)Math.rint(initialWithErrorBlue * b / 255) * 255 / b;
                changedBlue = FilterUtil.clampColor(changedBlue);

                Color changed = new Color(changedRed, changedGreen, changedBlue);


                result.setRGB(x, y, changed.getRGB());


                double errorRed = initialWithErrorRed - changed.getRed();
                double errorGreen = initialWithErrorGreen - changed.getGreen();
                double errorBlue = initialWithErrorBlue - changed.getBlue();



                if (x + 1 < result.getWidth())
                    error[y][x+1].plusValues(errorRed, errorGreen, errorBlue, (double)7/16);

                if (y + 1 < result.getHeight()) {
                    if (x - 1 >= 0)
                        error[y+1][x-1].plusValues(errorRed, errorGreen, errorBlue, (double)3/16);

                    error[y+1][x].plusValues(errorRed, errorGreen, errorBlue, (double)5/16);

                    if (x + 1 < result.getHeight())
                        error[y+1][x+1].plusValues(errorRed, errorGreen, errorBlue, (double)1/16);
                }
            }
        }

        return result;
    }
}
