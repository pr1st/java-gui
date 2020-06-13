package program.logic.actions.generalActions;

import program.gui.dialogs.MessageDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GetAboutAction extends AbstractAction {
    private static GetAboutAction getAboutAction;

    private GetAboutAction() {

    }

    public static GetAboutAction instance() {
        if (getAboutAction == null)
            getAboutAction = new GetAboutAction();
        return getAboutAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = "Isolines version 1.0    " +
                "  FIT Dymov Dmitry 16205";
        new MessageDialog("About program", text);
    }
}
