package program.beans.utilBeans.factoryForDialogs;


import program.beans.actions.pictureChangers.GammaCorrectionAction;
import program.beans.utilBeans.dialogs.GammaCorrectionParamDialog;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

@SingletonBean
public class GammaCorrectionParamDialogFactory {

    @Inject
    GammaCorrectionAction gammaCorrectionAction;


    public GammaCorrectionParamDialog getDialog() {
        GammaCorrectionParamDialog gammaCorrectionParamDialog = new GammaCorrectionParamDialog(gammaCorrectionAction);
        gammaCorrectionParamDialog.setLocationRelativeTo(null);
        gammaCorrectionParamDialog.pack();
        gammaCorrectionParamDialog.setVisible(true);
        return gammaCorrectionParamDialog;
    }
}
