package program.logic.actions;

import program.gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitAction extends AbstractAction {
    private static ExitAction exitAction;

    private ExitAction() {

    }

    public static ExitAction instance() {
        if (exitAction == null)
            exitAction = new ExitAction();
        return exitAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.instance().dispose();
    }
}
