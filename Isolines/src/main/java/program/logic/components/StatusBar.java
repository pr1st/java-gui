package program.logic.components;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class StatusBar extends JPanel {
    private static StatusBar statusBar;

    public static StatusBar instance() {
        if (statusBar == null)
            statusBar = new StatusBar();
        return statusBar;
    }

    private StatusBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));


        text = new JLabel();
        text.setBorder(new BevelBorder(BevelBorder.LOWERED));
        text.setPreferredSize(new Dimension(100, 20));

        add(text);
        setVisible(false);
    }


    private JLabel text;

    public void setMessage(String s) {
        text.setText(s);
    }
}
