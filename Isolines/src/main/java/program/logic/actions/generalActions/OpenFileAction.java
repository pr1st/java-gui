package program.logic.actions.generalActions;

import program.gui.dialogs.MessageDialog;
import program.logic.Configuration;
import program.logic.actions.businessActions.MouseControllerAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;

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

                Configuration c = new Configuration(fileReader);

                MainField.instance().setupField(c);
                LegendForField.instance().setupLegend(MainField.instance());
                MouseControllerAction.instance().setupController(c);
                MainField.instance().repaint();
                LegendForField.instance().repaint();
            }
        } catch (IOException ioEx) {
            new MessageDialog("Error", "Could not load configuration");
        } catch (IllegalArgumentException argEx) {
            new MessageDialog("Error", "Incorrect parameters for configuration");
        } finally {
            try {
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException e1) { }
        }
    }
}
