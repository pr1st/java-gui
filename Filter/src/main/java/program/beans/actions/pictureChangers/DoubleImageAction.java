package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;


@SingletonBean
public class DoubleImageAction extends AbstractAction {

    @Inject
    SecondComponent secondComponent;


    @Inject
    ThirdComponent thirdComponent;


    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(x2ImageInterpolation(secondComponent.getImage()));
    }


    public static BufferedImage x2ImageInterpolation(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        int center = 350 / 2;

        int delta = 350 / 4;

        for (int y = 0; y < image.getHeight(); y+=2) {
            for (int x = 0; x < image.getWidth(); x+=2) {
                int  c = new Color(image.getRGB(center - delta + x / 2, center - delta + y / 2)).getRGB();

                result.setRGB(x, y, c);
                if (x + 1 < result.getWidth())
                    result.setRGB(x + 1, y, c);
                if (y + 1 < result.getHeight()) {
                    result.setRGB(x, y + 1, c);

                    if (x + 1 < result.getWidth())
                        result.setRGB(x + 1, y + 1, c);
                }
            }
        }

        return result;
    }
}
