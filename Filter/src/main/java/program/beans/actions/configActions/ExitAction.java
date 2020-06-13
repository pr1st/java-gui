package program.beans.actions.configActions;

import program.beans.MainFrame;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;


@SingletonBean
public class ExitAction extends AbstractAction {

    @Inject
    MainFrame mainFrame;

    @Override
    public void actionPerformed(ActionEvent e) {
        mainFrame.dispose();
    }
}
