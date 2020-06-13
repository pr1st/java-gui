package program.logic.actions.generalActions;

import program.gui.dialogs.OptionsDialog;
import program.logic.Configuration;
import program.logic.actions.businessActions.MouseControllerAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OptionsAction extends AbstractAction {
    private static OptionsAction optionsAction;

    public static OptionsAction instance() {
        if (optionsAction == null)
            optionsAction = new OptionsAction();
        return optionsAction;
    }

    private OptionsAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new OptionsDialog(this::callback);
    }


    private void callback(Configuration configuration) {
        MouseControllerAction.instance().setupController(configuration);
        MainField.instance().setupField(configuration);
        LegendForField.instance().setupLegend(MainField.instance());
        MainField.instance().repaint();
        LegendForField.instance().repaint();
    }
}
