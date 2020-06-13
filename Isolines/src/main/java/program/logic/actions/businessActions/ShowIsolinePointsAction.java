package program.logic.actions.businessActions;

import program.logic.actions.ToggleAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import java.awt.event.ActionEvent;

public class ShowIsolinePointsAction extends ToggleAction {
    private static ShowIsolinePointsAction showIsolinePointsAction;

    public static ShowIsolinePointsAction instance() {
        if (showIsolinePointsAction == null)
            showIsolinePointsAction = new ShowIsolinePointsAction();
        return showIsolinePointsAction;
    }

    private ShowIsolinePointsAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        MainField.instance().repaint();
    }
}
