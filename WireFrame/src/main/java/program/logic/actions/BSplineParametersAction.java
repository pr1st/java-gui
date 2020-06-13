package program.logic.actions;

import program.gui.MainFrame;
import program.gui.dialogs.BSplineParametersDialog;
import program.logic.components.WorldComponent;
import program.logic.configs.BSplineConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BSplineParametersAction extends AbstractAction {
    private static BSplineParametersAction BSplineParametersAction;

    public static BSplineParametersAction instance() {
        if (BSplineParametersAction == null)
            BSplineParametersAction = new BSplineParametersAction();
        return BSplineParametersAction;
    }

    private BSplineParametersAction() {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (WorldComponent.instance().getConfig().selectedElementId != -1) {
            new BSplineParametersDialog(WorldComponent.instance().getConfig().elements.
                    get(WorldComponent.instance().getConfig().selectedElementId));
        } else {
            JOptionPane.showMessageDialog(MainFrame.instance(), "No BSpline selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
