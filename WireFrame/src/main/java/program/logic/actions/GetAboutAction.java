package program.logic.actions;

import program.gui.MainFrame;

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
        String text = "WireFrame version 1.0    " +
                "  FIT Dymov Dmitry 16205";
        JOptionPane.showMessageDialog(MainFrame.instance(), text);
    }
}
