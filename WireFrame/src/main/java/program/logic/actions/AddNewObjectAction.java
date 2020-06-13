package program.logic.actions;

import program.gui.dialogs.BSplineParametersDialog;
import program.gui.dialogs.PickCenterPointForObjectDialog;
import program.logic.Point3D;
import program.logic.components.WorldComponent;
import program.logic.configs.BSplineConfig;
import program.logic.configs.ProgramConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddNewObjectAction extends AbstractAction {
    private static AddNewObjectAction addNewObjectAction;

    public static AddNewObjectAction instance() {
        if (addNewObjectAction == null)
            addNewObjectAction = new AddNewObjectAction();
        return addNewObjectAction;
    }

    private AddNewObjectAction() {

    }

    private ProgramConfig saved;
    private BSplineConfig addingBspline;
    @Override
    public void actionPerformed(ActionEvent e) {
        saved = WorldComponent.instance().getConfig();
        ProgramConfig modify = saved.getCopy();
        addingBspline = new BSplineConfig();
        modify.elements.add(addingBspline);
        modify.selectedElementId = modify.elements.size() - 1;
        WorldComponent.instance().setConfig(modify);
        BSplineParametersDialog dialog = new BSplineParametersDialog(addingBspline);
        if (dialog.isChanged()) {
            addingBspline = dialog.getBSpline();
            new PickCenterPointForObjectDialog(this::callback, new Point3D(0,0,0));
        } else {
            WorldComponent.instance().setConfig(saved);
        }
    }

    private void callback(Point3D point) {
        if (point == null)
            WorldComponent.instance().setConfig(saved);
        addingBspline.centerPointInWorld = point;
        WorldComponent.instance().getConfig().selectedElementId = -1;
        WorldComponent.instance().setConfig(WorldComponent.instance().getConfig());
    }
}
