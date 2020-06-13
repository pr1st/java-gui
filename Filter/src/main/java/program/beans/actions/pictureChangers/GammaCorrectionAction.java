package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.beans.utilBeans.factoryForDialogs.GammaCorrectionParamDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.function.Function;


@SingletonBean
public class GammaCorrectionAction extends AbstractAction {

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Inject
    GammaCorrectionParamDialogFactory gammaCorrectionParamDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        gammaCorrectionParamDialogFactory.getDialog();
    }

    public void performFilter(double scale) {
        BufferedImage im = secondComponent.getImage();

        BufferedImage newIm = FilterUtil.applyForEachPixel(im, old  -> {

            Function<Integer, Integer> gammaCorrection = (c) -> {
                double oneToZero = (double)c / 255;
                double gammaCorrectedOneToZero = Math.pow(oneToZero, scale);
                return (int)(gammaCorrectedOneToZero * 255);
            };

            return new Color(
                    gammaCorrection.apply(old.getRed()),
                    gammaCorrection.apply(old.getGreen()),
                    gammaCorrection.apply(old.getBlue())
            );


        });

        thirdComponent.setImage(newIm);
    }
}
