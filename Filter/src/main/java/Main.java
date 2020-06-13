import program.beans.MainFrame;
import program.iocContainer.Container;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Container container = new Container(
                new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()), "");
        SwingUtilities.invokeLater(() -> container.getBean(MainFrame.class).setVisible(true));
    }
}
