package program.beans.actions.visualizationOfVolumeDensity;


import program.beans.components.AbsorptionGraphComponent;
import program.beans.components.EmissionGraphComponent;
import program.beans.utilBeans.factoryForDialogs.MessageDialogFactory;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


@SingletonBean
public class OpenConfigurationAction extends AbstractAction {


    @Inject
    MessageDialogFactory messageDialogFactory;

    @Inject
    RunRenderAction runRenderAction;

    @Inject
    AbsorptionGraphComponent absorptionGraphComponent;

    @Inject
    EmissionGraphComponent emissionGraphComponent;

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

        FileReader fileReader = null;


        int retVal = fileChooser.showOpenDialog(null);
        try {
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                fileReader = new FileReader(file);

                ConfigFile configFile = new ConfigFile(fileReader);
                runRenderAction.setConfigFile(configFile);
                absorptionGraphComponent.setAbsorptions(configFile.absorptions);
                emissionGraphComponent.setEmissions(configFile.emissions);

            }
        } catch (FileNotFoundException fileEx) {
            messageDialogFactory.getDialog("Error", "Could not find file");
        } catch (IOException ioEx) {
            messageDialogFactory.getDialog("Error", "Could not load configuration");
        } finally {
            try {
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException e1) { }
        }
    }
}
