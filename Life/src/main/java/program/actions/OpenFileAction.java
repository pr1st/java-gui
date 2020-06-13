package program.actions;

import program.Config;
import program.dialogs.MessageDialog;
import program.Field;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class OpenFileAction extends AbstractAction {

    JFileChooser fileChooser;

    Field field;

    public OpenFileAction(Field field) {
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int retVal = fileChooser.showOpenDialog(null);
        try {
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                FileReader reader = new FileReader(file);
                field.setNewConfig(new Config(reader));
                reader.close();


            }
        } catch (FileNotFoundException fileEx) {
            fileEx.printStackTrace();
        } catch (IOException ioEx) {
            MessageDialog dialog = new MessageDialog("Cant read file");
        }

    }
}
