package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@SingletonBean
public class ToBlackAndWhiteAction extends AbstractAction {

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;


    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(toBlackAndWhiteImage(secondComponent.getImage()));
    }

    public static BufferedImage toBlackAndWhiteImage(BufferedImage image) {
        return FilterUtil.applyForEachPixel(image, old -> {
            int greyValue = (int)(old.getRed() * 0.299) + (int)(old.getGreen()*0.587) + (int)(old.getBlue() * 0.144);

            greyValue = FilterUtil.clampColor(greyValue);

            return new Color(greyValue, greyValue, greyValue);
        });
    }

}
