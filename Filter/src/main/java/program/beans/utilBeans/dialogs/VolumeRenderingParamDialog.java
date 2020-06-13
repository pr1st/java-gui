package program.beans.utilBeans.dialogs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class VolumeRenderingParamDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinnerX;
    private JSpinner spinnerY;
    private JSpinner spinnerZ;

    private Consumer<Integer[]> callabck;

    public VolumeRenderingParamDialog(Consumer<Integer[]> callback) {
        this.callabck = callback;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Volume rendering options");

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
        configSpinner(spinnerX, 1, 350, 350);
        configSpinner(spinnerY, 1, 350, 350);
        configSpinner(spinnerZ, 1, 350, 350);
    }

    private void onOK() {
        callabck.accept(new Integer[] {
                (Integer)spinnerX.getValue(),
                (Integer)spinnerY.getValue(),
                (Integer)spinnerZ.getValue()}
                );
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
