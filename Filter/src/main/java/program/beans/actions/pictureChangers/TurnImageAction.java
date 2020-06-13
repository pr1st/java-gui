package program.beans.actions.pictureChangers;


import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.beans.utilBeans.factoryForDialogs.TurnImageParamDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

@SingletonBean
public class TurnImageAction extends AbstractAction {

    @Inject
    SecondComponent secondComponent;


    @Inject
    ThirdComponent thirdComponent;

    @Inject
    TurnImageParamDialogFactory turnImageParamDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        turnImageParamDialogFactory.getDialog(this::turnImage);
    }

    public void turnImage(int angle) {
        thirdComponent.setImage(turnImage(secondComponent.getImage(), angle));
    }


    public static BufferedImage turnImage (BufferedImage image, int angle) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));


        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;


        for (int y = -centerY; y < centerY; y++) {
            for (int x = - centerX; x < centerX; x++) {
                int newX = (int)(x * cos + y * sin);
                int newY = (int)(y * cos - x * sin);


                if (newX + centerX < 0 || newX + centerX >= image.getWidth()
                        || newY + centerY < 0 || newY + centerY >= image.getHeight())
                    result.setRGB(x + centerX, y + centerY, Color.WHITE.getRGB());
                else
                    result.setRGB(x + centerX, y + centerY, image.getRGB(newX + centerX, newY + centerY));

            }
        }

        return result;
    }
}
