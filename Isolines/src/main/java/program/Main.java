package program;

import program.gui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> MainFrame.instance().setVisible(true));
    }
}
