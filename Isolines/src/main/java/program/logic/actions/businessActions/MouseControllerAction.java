package program.logic.actions.businessActions;

import program.MainFunction;
import program.logic.Configuration;
import program.logic.ProgramUtil;
import program.logic.components.MainField;
import program.logic.components.StatusBar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

public class MouseControllerAction extends MouseAdapter {
    private static MouseControllerAction mouseControllerAction;

    private Configuration configuration;
    private DecimalFormat formatter = new DecimalFormat("#.#");

    public static MouseControllerAction instance() {
        if (mouseControllerAction == null)
            mouseControllerAction = new MouseControllerAction();
        return mouseControllerAction;
    }

    private MouseControllerAction() {

    }

    public void setupController(Configuration configuration) {
        this.configuration = configuration;
    }


    private Point2D getPointOfLocationInField(MouseEvent e) {
        return ProgramUtil.toRealValue(configuration, e.getX(), e.getY(), MainField.instance().FIELD_WIDTH, MainField.instance().FIELD_HEIGHT);
    }


    private void setStatus(Point2D p, double z) {
        StatusBar.instance().setMessage("Point (" + formatter.format(p.getX()) + ", " + formatter.format(p.getY()) + ") where z = " + formatter.format(z));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point2D p = getPointOfLocationInField(e);
        double z = MainFunction.f(p.getX(), p.getY());
        MainField.instance().createUserIsoline(z);
        MainField.instance().repaint();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        Point2D p = getPointOfLocationInField(e);
        double z = MainFunction.f(p.getX(), p.getY());
        setStatus(p, z);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D p = getPointOfLocationInField(e);
        double z = MainFunction.f(p.getX(), p.getY());
        setStatus(p, z);
        MainField.instance().moveDynamicIsoline(z);
        MainField.instance().repaint();
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        StatusBar.instance().setVisible(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        StatusBar.instance().setVisible(false);
    }
}
