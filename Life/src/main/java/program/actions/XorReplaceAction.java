package program.actions;


import program.Field;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class XorReplaceAction extends AbstractAction{

    private List<AbstractButton> xorButtons;
    private List<AbstractButton> replaceButtons;

    private Field field;

    private static XorReplaceAction instance;

    public static XorReplaceAction getInstance() {
        if (instance == null) {
            instance = new XorReplaceAction();
        }
        return instance;
    }

    private XorReplaceAction() {
        xorButtons = new ArrayList<>();
        replaceButtons = new ArrayList<>();
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void addXorButton(AbstractButton xor) {
        xorButtons.add(xor);
    }

    public void addReplaceButton(AbstractButton replace) {
        replaceButtons.add(replace);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("xor".equals(e.getActionCommand())) {
            field.getConfig().xorOption = true;
            xorButtons.forEach(b -> b.setSelected(true));
            replaceButtons.forEach(b -> b.setSelected(false));
        } else if ("replace".equals(e.getActionCommand())) {
            field.getConfig().xorOption = false;
            xorButtons.forEach(b -> b.setSelected(false));
            replaceButtons.forEach(b -> b.setSelected(true));
        }
    }
}
