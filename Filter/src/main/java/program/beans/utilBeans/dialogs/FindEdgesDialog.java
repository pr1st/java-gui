package program.beans.utilBeans.dialogs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class FindEdgesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinner;
    private JSlider slider;


    private Consumer<Integer> callbackResultHere;

    public FindEdgesDialog(Consumer<Integer> callbackResultHere) {
        this.callbackResultHere = callbackResultHere;

        setTitle("Find Edges");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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


        configSpinnerAndSlider(spinner, slider, 0, 255, 40);
    }

    private void onOK() {
        callbackResultHere.accept((Integer)spinner.getValue());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void configSpinnerAndSlider(JSpinner spinner, JSlider slider, int minVal, int maxVal, int curVal) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        spinner.setValue(curVal);


        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = (Integer) spinner.getValue();
                if (val < minVal)
                    val = minVal;
                if (val > maxVal)
                    val = maxVal;

                slider.setValue(val);
                spinner.setValue(val);
            }
        });

        slider.setMinimum(minVal);
        slider.setMaximum(maxVal);
        slider.setValue((Integer) curVal);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                spinner.setValue(slider.getValue());
            }
        });
    }
}
