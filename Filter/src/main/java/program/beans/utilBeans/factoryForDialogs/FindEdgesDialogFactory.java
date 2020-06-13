package program.beans.utilBeans.factoryForDialogs;


import program.beans.utilBeans.dialogs.FindEdgesDialog;
import program.iocContainer.SingletonBean;

import java.util.function.Consumer;

@SingletonBean
public class FindEdgesDialogFactory {


    public FindEdgesDialog getDialog(Consumer<Integer> callback) {
        FindEdgesDialog findEdgesDialog = new FindEdgesDialog(callback);
        findEdgesDialog.setLocationRelativeTo(null);
        findEdgesDialog.pack();
        findEdgesDialog.setVisible(true);
        return findEdgesDialog;
    }
}
