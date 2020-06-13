package program.beans.actions.configActions;

import program.beans.utilBeans.factoryForDialogs.MessageDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;


@SingletonBean
public class GetAboutAction extends AbstractAction {

    @Inject
    MessageDialogFactory messageDialogFactory;


    @Override
    public void actionPerformed(ActionEvent e) {
        String text = "Filter version 1.0    " +
                "  FIT Dymov Dmitry 16205";
        messageDialogFactory.getDialog("About program", text);
    }
}
