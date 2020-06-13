package program.actions;

import program.Config;
import program.Field;
import program.dialogs.MessageDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class SaveFileAction extends AbstractAction {

    JFileChooser fileChooser;
    Field field;

    public SaveFileAction(Field field) {
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int retVal = fileChooser.showSaveDialog(null);
        try {
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file.exists())
                    file.delete();
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(field.getConfig().toString());

                writer.close();
            }
        } catch (FileNotFoundException fileEx) {
            MessageDialog dialog = new MessageDialog("File not saved");
        } catch (IOException ioEx) {
            MessageDialog dialog = new MessageDialog("File not saved");
        }
    }
}
