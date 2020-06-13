package program.beans.actions.configActions;

import program.beans.components.AbstractJComponent;
import program.beans.components.FirstComponent;
import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


@SingletonBean
public class NewDocAction extends AbstractAction {

    @Inject
    FirstComponent firstComponent;

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        firstComponent.restoreImage();
        secondComponent.restoreImage();
        thirdComponent.restoreImage();
    }
}
