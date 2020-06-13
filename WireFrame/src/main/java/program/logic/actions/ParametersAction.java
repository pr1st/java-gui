package program.logic.actions;

import program.gui.dialogs.ParametersDialog;
import program.logic.configs.ProgramConfig;
import program.logic.components.WorldComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ParametersAction extends AbstractAction {
    private static ParametersAction parametersAction;

    public static ParametersAction instance() {
        if (parametersAction == null)
            parametersAction = new ParametersAction();
        return parametersAction;
    }

    private ParametersAction() {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        new ParametersDialog(this::getConfig);
    }

    private void getConfig(ProgramConfig config) {
        WorldComponent.instance().setConfig(config);
    }
}
