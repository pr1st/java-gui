package program.logic.actions;


import program.logic.components.WorldComponent;
import program.logic.configs.ProgramConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OpenFileAction extends AbstractAction {
    private static OpenFileAction openFileAction;

    private OpenFileAction() {

    }

    public static OpenFileAction instance() {
        if (openFileAction == null)
            openFileAction = new OpenFileAction();
        return openFileAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

        FileReader fileReader = null;
        int retVal = fileChooser.showOpenDialog(null);
        try {
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                fileReader = new FileReader(file);

                ProgramConfig c = new ProgramConfig(fileReader);

                WorldComponent.instance().setConfig(c);
            }
        } catch (IOException ioEx) {
            JOptionPane.showMessageDialog(WorldComponent.instance(), "Could not load configuration", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException argEx) {
            JOptionPane.showMessageDialog(WorldComponent.instance(), argEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException e1) { }
        }
    }
}
