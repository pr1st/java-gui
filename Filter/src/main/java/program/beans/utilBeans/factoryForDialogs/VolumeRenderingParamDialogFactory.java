package program.beans.utilBeans.factoryForDialogs;


import program.beans.utilBeans.dialogs.VolumeRenderingParamDialog;
import program.iocContainer.SingletonBean;

import java.util.function.Consumer;

@SingletonBean
public class VolumeRenderingParamDialogFactory {

    public VolumeRenderingParamDialog getDialog(Consumer<Integer[]> callback) {
        VolumeRenderingParamDialog volumeRenderingParamDialog = new VolumeRenderingParamDialog(callback);
        volumeRenderingParamDialog.setLocationRelativeTo(null);
        volumeRenderingParamDialog.pack();
        volumeRenderingParamDialog.setVisible(true);
        return volumeRenderingParamDialog;
    }
}
