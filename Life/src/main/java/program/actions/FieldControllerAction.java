package program.actions;

import program.Field;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class FieldControllerAction extends AbstractAction {
    private static FieldControllerAction instance = new FieldControllerAction();

    private List<AbstractButton> buttonsToDisableWhileRun = new ArrayList<>();


    private Field field;
    private Timer timer;



    public static FieldControllerAction getInstance() {
        return instance;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void addButtonsToDisableWhileRun(AbstractButton button) {
        buttonsToDisableWhileRun.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("impact".equals(e.getActionCommand())) {
            field.changeImpactShowing();
        } else if ("restart".equals(e.getActionCommand())) {
            field.restart();
        } else if ("next".equals(e.getActionCommand())) {
            field.nextStep();
        } else if ("run".equals(e.getActionCommand())) {
            if (timer == null || !timer.isRunning()) {
                timer = new Timer(100, this::run);
                buttonsToDisableWhileRun.forEach((b) -> b.setEnabled(false));
                field.changeFieldByMouse = false;
                timer.start();
            } else {
                timer.stop();
                field.changeFieldByMouse = true;
                buttonsToDisableWhileRun.forEach((b) -> b.setEnabled(true));
            }
        }
    }


    public void run(ActionEvent e) {
        field.nextStep();
    }
}
