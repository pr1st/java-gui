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
public class EmbossAction extends AbstractAction {
    int[][] filterMatrix = {
            {0, 1, 0},
            {-1, 0, 1},
            {0, -1, 0}
    };


    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(FilterUtil.applyFilter(secondComponent.getImage(), filterMatrix, 1, (c) -> c + 128));
    }
}
