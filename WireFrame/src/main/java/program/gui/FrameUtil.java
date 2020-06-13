package program.gui;

import javax.swing.*;
import java.awt.*;

class FrameUtil {
    static JButton getConfiguredButtonToToolbar(AbstractAction action, String iconName, String toolTipText) {
        JButton button = new JButton();
        button.addActionListener(action);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/" + iconName).getPath()));
        button.setToolTipText(toolTipText);
        return button;
    }


    static JMenuItem getConfiguredMenuItem(AbstractAction action, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }

    static void setCenterScreenLocation(JFrame frame) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        frame.setLocation(x, y - 20);
    }
}
