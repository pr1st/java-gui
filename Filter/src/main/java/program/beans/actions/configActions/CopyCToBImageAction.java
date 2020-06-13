package program.beans.actions.configActions;

import program.beans.actions.pictureChangers.FilterUtil;
import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

@SingletonBean
public class CopyCToBImageAction extends AbstractAction {
    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        secondComponent.setImage(FilterUtil.getCopy(thirdComponent.getImage()));
    }
}
