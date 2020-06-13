package program.actions;

import program.dialogs.MessageDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutProgramAction extends AbstractAction {


    @Override
    public void actionPerformed(ActionEvent e) {
        String text = "Life version 1.0    " +
                "  FIT Dymov Dmitry 16205";
        MessageDialog dialog = new MessageDialog(text);
    }
}
