package program.beans.utilBeans.factoryForDialogs;


import program.beans.actions.pictureChangers.FloydDitheringAction;
import program.beans.utilBeans.dialogs.FloydDitheringParamDialog;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

@SingletonBean
public class FloydDitheringParamDialogFactory {

    @Inject
    FloydDitheringAction floydDitheringAction;


    public FloydDitheringParamDialog getDialog() {
        FloydDitheringParamDialog floydDitheringParamDialog = new FloydDitheringParamDialog(floydDitheringAction);
        floydDitheringParamDialog.setLocationRelativeTo(null);
        floydDitheringParamDialog.pack();
        floydDitheringParamDialog.setVisible(true);
        return floydDitheringParamDialog;
    }
}
