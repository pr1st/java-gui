package program.beans.utilBeans.dialogs;

import program.beans.actions.pictureChangers.GammaCorrectionAction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;

public class GammaCorrectionParamDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinner1;

    private GammaCorrectionAction gammaCorrectionAction;

    private boolean error;

    public GammaCorrectionParamDialog(GammaCorrectionAction gammaCorrectionAction) {
        this.gammaCorrectionAction = gammaCorrectionAction;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Gamma correction");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        configSpinner(spinner1, 0.01, 10.0, 1.0);
    }

    private void onOK() {
        gammaCorrectionAction.performFilter((Double) spinner1.getValue());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void configSpinner(JSpinner spinner, double minVal, double maxVal, double curVal) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });


        spinner.setModel(new SpinnerNumberModel(curVal, minVal, maxVal, 0.01));
    }
}
