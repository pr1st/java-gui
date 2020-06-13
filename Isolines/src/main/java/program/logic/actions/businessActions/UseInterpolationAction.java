package program.logic.actions.businessActions;

import program.logic.actions.ToggleAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import java.awt.event.ActionEvent;

public class UseInterpolationAction extends ToggleAction {
    private static UseInterpolationAction useInterpolationAction;

    private UseInterpolationAction() {

    }

    public static UseInterpolationAction instance() {
        if (useInterpolationAction == null)
            useInterpolationAction = new UseInterpolationAction();
        return useInterpolationAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        MainField.instance().repaint();
        LegendForField.instance().repaint();
    }
}
