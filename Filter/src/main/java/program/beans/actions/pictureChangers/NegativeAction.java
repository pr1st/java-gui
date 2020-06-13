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
public class NegativeAction extends AbstractAction {
    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(FilterUtil.applyForEachPixel(secondComponent.getImage(), old -> {
            return new Color(255 - old.getRed(), 255 - old.getGreen(), 255 - old.getBlue());
        }));
    }
}
