package program.logic.actions;

import program.gui.dialogs.PickObjectDialog;
import program.logic.components.WorldComponent;
import program.logic.configs.ProgramConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickObjectAction extends AbstractAction {
    private static PickObjectAction pickObjectAction;
    
    public static PickObjectAction instance() {
        if (pickObjectAction == null)
            pickObjectAction = new PickObjectAction();
        return pickObjectAction;
    }
    
    private PickObjectAction() {
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new PickObjectDialog(this::callback, WorldComponent.instance().getConfig().elements, WorldComponent.instance().getConfig().selectedElementId);
    }

    private void callback(Integer id) {
        ProgramConfig c = WorldComponent.instance().getConfig();
        if (id == null)
            c.selectedElementId = -1;
        else
            c.selectedElementId = id;
        WorldComponent.instance().setConfig(c);
    }
}
