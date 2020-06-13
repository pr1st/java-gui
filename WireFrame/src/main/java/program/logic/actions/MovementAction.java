package program.logic.actions;

import program.gui.dialogs.PickCenterPointForObjectDialog;
import program.logic.Matrix4X4;
import program.logic.components.WorldComponent;
import program.logic.configs.ProgramConfig;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static java.lang.Math.*;

public class MovementAction extends MouseAdapter {
    private static MovementAction movementAction;

    public static MovementAction instance() {
        if (movementAction == null)
            movementAction = new MovementAction();
        return movementAction;
    }

    private MovementAction() {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final double DELTA_FOR_SCROLL = 0.2;
        ProgramConfig c = WorldComponent.instance().getConfig();
        if (DELTA_FOR_SCROLL * e.getWheelRotation() + c.zn < 0)
            return;
        c.zn += DELTA_FOR_SCROLL * e.getWheelRotation();
        WorldComponent.instance().setConfig(c);
    }



    private Point prevPoint = null;


    @Override
    public void mouseClicked(MouseEvent e) {
        ProgramConfig c = WorldComponent.instance().getConfig();
        if (c.selectedElementId != -1) {
            if (e.isControlDown()) {
                new PickCenterPointForObjectDialog((p) -> {
                    if (p != null) {
                        c.elements.get(c.selectedElementId).centerPointInWorld = p;
                        WorldComponent.instance().setConfig(c);
                    }
                }, c.elements.get(c.selectedElementId).centerPointInWorld);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ProgramConfig c = WorldComponent.instance().getConfig();
        double currentPhi;
        double currentXi;
        if (c.selectedElementId == -1) {
            currentPhi = acos(c.worldRotationMatrix.getMatrix()[0][0]);
            currentXi = acos(c.worldRotationMatrix.getMatrix()[1][1]);
        } else {
            currentPhi = acos(c.elements.get(c.selectedElementId).rotationMatrix.getMatrix()[0][0]);
            currentXi = acos(c.elements.get(c.selectedElementId).rotationMatrix.getMatrix()[1][1]);
        }
        final double DELTA_FOR_MOVEMENT_ANGLE = 0.1;
        if (prevPoint != null) {
            Matrix4X4 aroundX = new Matrix4X4(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
            });
            Matrix4X4 aroundY = new Matrix4X4(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
            });

            if (prevPoint.x != e.getX()) {
                currentPhi += DELTA_FOR_MOVEMENT_ANGLE * signum(e.getX() - prevPoint.x);
                currentPhi = currentPhi > PI ? PI : currentPhi < 0 ? 0 : currentPhi;
            }
            if (prevPoint.y != e.getY()) {
                currentXi += DELTA_FOR_MOVEMENT_ANGLE * signum(e.getY() - prevPoint.y);
                currentXi = currentXi > PI ? PI : currentXi < 0 ? 0 : currentXi;
            }
            aroundX = new Matrix4X4(new double[][]{
                    {1, 0, 0, 0},
                    {0, cos(currentXi), -sin(currentXi), 0},
                    {0, sin(currentXi), cos(currentXi), 0},
                    {0, 0, 0, 1}
            });
            aroundY = new Matrix4X4(new double[][]{
                    {cos(currentPhi), 0, sin(currentPhi), 0},
                    {0, 1, 0, 0},
                    {-sin(currentPhi), 0, cos(currentPhi), 0},
                    {0, 0, 0, 1}
            });
            if (c.selectedElementId == -1) {
                c.worldRotationMatrix = aroundX.multiply(aroundY);
            } else {
                c.elements.get(c.selectedElementId).rotationMatrix = aroundX.multiply(aroundY);
            }
            WorldComponent.instance().setConfig(c);
        }
        prevPoint = new Point(e.getX(), e.getY());
    }
}