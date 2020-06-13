package program.beans.utilBeans.dialogs;

import program.beans.actions.pictureChangers.FloydDitheringAction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;



public class FloydDitheringParamDialog extends JDialog {


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JSpinner redSpinner;
    private JSpinner greenSpinner;
    private JSpinner blueSpinner;

    private FloydDitheringAction floydDitheringAction;

    public FloydDitheringParamDialog(FloydDitheringAction floydDitheringAction) {
        this.floydDitheringAction = floydDitheringAction;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Floyd Dithering");

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

        configSpinner(redSpinner, 2, 255, 2);
        configSpinner(greenSpinner, 2, 255, 2);
        configSpinner(blueSpinner, 2, 255, 2);
    }

    private void onOK() {
        floydDitheringAction.performDither(
                (Integer)redSpinner.getValue(),
                (Integer)greenSpinner.getValue(),
                (Integer)blueSpinner.getValue());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void configSpinner(JSpinner spinner, int minVal, int maxVal, int curVal) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = (Integer)spinner.getValue();
                if (val < minVal)
                    val = minVal;
                if (val > maxVal)
                    val = maxVal;
                spinner.setValue(val);
            }
        });
        spinner.setValue(curVal);
    }
}
