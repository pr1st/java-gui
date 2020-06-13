package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SingletonBean
public class BlurFilterAction extends AbstractAction {


    int[][] filterMatrix = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    double scale = (double)1/9;

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(FilterUtil.applyFilter(secondComponent.getImage(), filterMatrix, scale, null));
    }
}
