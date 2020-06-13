package program.actions;

import program.Field;
import program.dialogs.OptionsDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class OptionAction extends AbstractAction {

    Field field;

    public OptionAction(Field field) {
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptionsDialog optionsDialog = new OptionsDialog(field);
        optionsDialog.pack();
        optionsDialog.setVisible(true);
    }
}
