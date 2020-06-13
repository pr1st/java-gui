package program.logic.actions.businessActions;

import program.logic.actions.ToggleAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import java.awt.event.ActionEvent;

public class ShowIsolinesAction extends ToggleAction {
    private static ShowIsolinesAction showIsolinesAction;

    private ShowIsolinesAction() {

    }

    public static ShowIsolinesAction instance() {
        if (showIsolinesAction == null)
            showIsolinesAction = new ShowIsolinesAction();
        return showIsolinesAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        MainField.instance().repaint();
        LegendForField.instance().repaint();
    }
}
