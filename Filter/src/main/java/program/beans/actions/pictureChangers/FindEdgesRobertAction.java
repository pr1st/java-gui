package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.beans.utilBeans.factoryForDialogs.FindEdgesDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;


@SingletonBean
public class FindEdgesRobertAction extends AbstractAction {

    int[][] xRobertMatrix = {
            {0, 0, 0},
            {0, -1, 0},
            {0, 0, 1}
    };

    int[][] yRobertMatrix = {
            {0, 0, 0},
            {0, 0, -1},
            {0, 1, 0}
    };

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;


    @Inject
    FindEdgesDialogFactory findEdgesDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        findEdgesDialogFactory.getDialog(this::performFilter);
    }

    public void performFilter(int limit) {
        thirdComponent.setImage(FilterUtil.findEdgesFilter(secondComponent.getImage(), xRobertMatrix, yRobertMatrix, limit));

    }
}
