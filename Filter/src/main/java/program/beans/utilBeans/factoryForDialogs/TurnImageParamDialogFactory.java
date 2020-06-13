package program.beans.utilBeans.factoryForDialogs;


import program.beans.utilBeans.dialogs.TurnImageParamDialog;
import program.iocContainer.SingletonBean;

import java.util.function.Consumer;

@SingletonBean
public class TurnImageParamDialogFactory {
    public TurnImageParamDialog getDialog(Consumer<Integer> callback) {
        TurnImageParamDialog turnImageParamDialog = new TurnImageParamDialog(callback);
        turnImageParamDialog.setLocationRelativeTo(null);
        turnImageParamDialog.pack();
        turnImageParamDialog.setVisible(true);
        return turnImageParamDialog;
    }
}
