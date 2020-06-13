package program.gui.dialogs;

import program.logic.Configuration;
import program.logic.components.MainField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class OptionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner kSpinner;
    private JSpinner mSpinner;
    private JSpinner dSpinner;
    private JSpinner aSpinner;
    private JSpinner bSpinner;
    private JSpinner cSpinner;

    private Consumer<Configuration> callback;
    public OptionsDialog(Consumer<Configuration> callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        setTitle("Options");

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

        Configuration c = MainField.instance().getConfiguration();

        configSpinner(kSpinner, c.k);
        configSpinner(mSpinner, c.m);
        configSpinner(aSpinner, c.a);
        configSpinner(bSpinner, c.b);
        configSpinner(cSpinner, c.c);
        configSpinner(dSpinner, c.d);



        pack();
        setVisible(true);
    }

    private void onOK() {
        if (!validateParameters()) {
            new MessageDialog("Error", "Illegal parameters for corners");
            return;
        }

        Configuration c = MainField.instance().getConfiguration();

        c.k = (Integer)kSpinner.getValue();
        c.m = (Integer)mSpinner.getValue();
        c.a = (Double)aSpinner.getValue();
        c.b = (Double)bSpinner.getValue();
        c.c = (Double)cSpinner.getValue();
        c.d = (Double)dSpinner.getValue();

        callback.accept(c);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void configSpinner(JSpinner spinner, int curVal) {
        removeArrows(spinner);
        spinner.setModel(new SpinnerNumberModel(curVal, 2, 1000, 1));

    }

    private void configSpinner(JSpinner spinner, double curVal) {
        removeArrows(spinner);
        spinner.setModel(new SpinnerNumberModel(curVal, -999, 999, 0.1));
    }

    private void removeArrows(JSpinner spinner) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });
    }

    private boolean validateParameters() {
        if ((Double)aSpinner.getValue() >= (Double)bSpinner.getValue()
        || (Double)cSpinner.getValue() >= (Double)dSpinner.getValue())
            return false;
        return true;
    }
}
