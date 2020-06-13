package program.logic.actions.businessActions;

import program.logic.actions.ToggleAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

import java.awt.event.ActionEvent;

public class ShowMapAction extends ToggleAction {
    private static ShowMapAction showMapAction;

    private ShowMapAction() {

    }

    public static ShowMapAction instance() {
        if (showMapAction == null)
            showMapAction = new ShowMapAction();
        return showMapAction;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        MainField.instance().repaint();
        LegendForField.instance().repaint();
    }
}
