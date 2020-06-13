package program.beans.actions.visualizationOfVolumeDensity;


import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


@SingletonBean
public class EnableEmissionAction extends AbstractAction {


    private List<AbstractButton> buttonsToSelect = new ArrayList<>();

    private boolean isActivated = false;

    public void addToggleButton(AbstractButton button) {
        buttonsToSelect.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        isActivated = !isActivated;
        if (isActivated)
            buttonsToSelect.forEach((b) -> b.setSelected(true));
        else
            buttonsToSelect.forEach((b) -> b.setSelected(false));
    }

    public boolean isActivated() {
        return isActivated;
    }
}
