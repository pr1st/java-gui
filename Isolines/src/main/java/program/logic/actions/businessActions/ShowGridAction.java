package program.logic.actions.businessActions;

import program.logic.actions.ToggleAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import java.awt.event.ActionEvent;

public class ShowGridAction extends ToggleAction {
    private static ShowGridAction showGridAction;

    private ShowGridAction() {

    }

    public static ShowGridAction instance() {
        if (showGridAction == null)
            showGridAction = new ShowGridAction();
        return showGridAction;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        MainField.instance().repaint();
        LegendForField.instance().repaint();
    }
}
