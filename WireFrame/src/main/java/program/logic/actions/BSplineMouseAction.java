package program.logic.actions;

import program.logic.configs.BSplineConfig;
import program.logic.components.BSplineComponent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class BSplineMouseAction extends MouseAdapter {

    private BSplineComponent component;

    private Point2D point;
    private boolean isPressed = false;

    private boolean isInField;
    @Override
    public void mouseEntered(MouseEvent e) {
        isInField = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isInField = false;
    }

    public BSplineMouseAction(BSplineComponent component) {
        this.component = component;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D point = component.getImage().getSupportingPoint(e.getX(), e.getY());
        if (point != null) {
            this.point = point;
            isPressed = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isPressed && isInField) {
            BSplineConfig config = component.getConfig();
            int id = config.points.indexOf(point);
            if (id == -1)
                throw new IllegalStateException();

            config.points.set(id, component.getImage().toRealValue(e.getX(), e.getY()));
            point = config.points.get(id);
            component.setConfig(config);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
    }
}
