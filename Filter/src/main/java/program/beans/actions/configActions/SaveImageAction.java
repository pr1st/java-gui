package program.beans.actions.configActions;

import program.beans.components.ThirdComponent;
import program.beans.utilBeans.factoryForDialogs.MessageDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



@SingletonBean
public class SaveImageAction extends AbstractAction {


    @Inject
    ThirdComponent thirdComponent;

    @Inject
    MessageDialogFactory messageDialogFactory;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (thirdComponent.isImageSet) {
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
                    return "*.bmp or *.png, default value: *.png";
                }
            });

            int retVal = fileChooser.showSaveDialog(null);

            try {
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (file.exists())
                        file.delete();
                    if (!(file.getName().endsWith(".bmp") || file.getName().endsWith(".png")))
                        file = new File(file.getName() + ".png");
                    file.createNewFile();

                    if (file.getName().endsWith(".png"))
                        ImageIO.write(thirdComponent.getImage(), "png", file);
                    else
                        ImageIO.write(thirdComponent.getImage(), "bmp", file);

                }
            } catch (FileNotFoundException fileEx) {
                messageDialogFactory.getDialog("Error", "Could not find file");
            } catch (IOException ioEx) {
                messageDialogFactory.getDialog("Error", "Could not save image");
            }
        } else {
            messageDialogFactory.getDialog("Error", "Third image is not set");
        }
    }
}
