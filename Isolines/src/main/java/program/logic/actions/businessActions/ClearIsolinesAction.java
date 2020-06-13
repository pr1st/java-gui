package program.logic.actions.businessActions;

import program.logic.components.MainField;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ClearIsolinesAction extends AbstractAction {
    private static ClearIsolinesAction clearIsolinesAction;

    private ClearIsolinesAction() {

    }

    public static ClearIsolinesAction instance() {
        if (clearIsolinesAction == null)
            clearIsolinesAction = new ClearIsolinesAction();
        return clearIsolinesAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainField.instance().clearUserIsolines();
        MainField.instance().repaint();
    }
}
