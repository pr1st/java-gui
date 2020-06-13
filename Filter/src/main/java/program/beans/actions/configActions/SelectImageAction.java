package program.beans.actions.configActions;

import program.beans.components.FirstComponent;
import program.beans.components.SecondComponent;
import program.iocContainer.Inject;
import program.iocContainer.PostConstruct;
import program.iocContainer.SingletonBean;


import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


@SingletonBean
public class SelectImageAction extends AbstractAction {


    @Inject
    private FirstComponent firstComponent;

    @Inject
    private SecondComponent secondComponent;


    private List<AbstractButton> buttonsToSelect = new ArrayList<>();

    private boolean isActivated = false;

    public void addToggleButton(AbstractButton button) {
        buttonsToSelect.add(button);
    }

    MouseMotionListener mouseMotionListener;
    MouseListener mouseListener;
    @PostConstruct
    private void construct() {
        mouseMotionListener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseAction(e.getX(),e.getY());
            }
        };
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseAction(e.getX(),e.getY());
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        isActivated = !isActivated;
        if (isActivated) {
            firstComponent.addMouseMotionListener(mouseMotionListener);
            firstComponent.addMouseListener(mouseListener);
            buttonsToSelect.forEach((b) -> b.setSelected(true));
        } else {
            firstComponent.removeMouseMotionListener(mouseMotionListener);
            firstComponent.removeMouseListener(mouseListener);

            buttonsToSelect.forEach((b) -> b.setSelected(false));

        }
    }

    private void handleMouseAction(int x, int y) {
        BufferedImage image = firstComponent.selectArea(x, y);
        secondComponent.setImage(image);
    }
}
