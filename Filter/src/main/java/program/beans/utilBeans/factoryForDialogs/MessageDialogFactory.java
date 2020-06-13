package program.beans.utilBeans.factoryForDialogs;


import program.beans.utilBeans.dialogs.MessageDialog;
import program.iocContainer.SingletonBean;

@SingletonBean
public class MessageDialogFactory {

    public MessageDialog getDialog(String title, String message) {
        MessageDialog dialog = new MessageDialog(title, message);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
