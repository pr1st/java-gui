package program.beans.actions.configActions;


import program.beans.components.FirstComponent;
import program.beans.utilBeans.factoryForDialogs.MessageDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


@SingletonBean
public class LoadImageAction extends AbstractAction {

    @Inject
    FirstComponent component;

    @Inject
    MessageDialogFactory messageDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".bmp") || f.getName().endsWith(".png")
                        || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "*.bmp or *.png";
            }
        });

        int retVal = fileChooser.showOpenDialog(null);
        try {
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                if (image == null)
                    throw new IOException();
                component.setImage(image);


            }
        } catch (FileNotFoundException fileEx) {
            messageDialogFactory.getDialog("Error", "Could not find file");
        } catch (IOException ioEx) {
            messageDialogFactory.getDialog("Error", "Could not load image");

        }
    }
}
