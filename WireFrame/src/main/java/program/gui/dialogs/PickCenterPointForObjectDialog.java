package program.gui.dialogs;

import program.logic.Point3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;

public class PickCenterPointForObjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JSpinner zSpinner;


    private Consumer<Point3D> callback;
    public PickCenterPointForObjectDialog(Consumer<Point3D> callback, Point3D startingVal) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("CenterPoint Pick Up");

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

        configSpinner(xSpinner, -100, 100, startingVal.getX());
        configSpinner(ySpinner, -100, 100, startingVal.getY());
        configSpinner(zSpinner, -100, 100, startingVal.getZ());

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void onOK() {
        callBackAccept();
        dispose();
    }

    private void onCancel() {
        callback.accept(null);
        dispose();
    }

    private void callBackAccept() {
        callback.accept(new Point3D(
                (Double)xSpinner.getValue(),
                (Double)ySpinner.getValue(),
                (Double)zSpinner.getValue()
        ));
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

    private double getDouble(JSpinner spinner) {
        double val;
        try {
            System.out.println("first");
            val = (Double)spinner.getValue();
        } catch (ClassCastException e) {
            try {
                System.out.println("second");
                val = Double.parseDouble((String) spinner.getValue());
            } catch (ClassCastException ex) {
                System.out.println("third");
                val = (Integer)spinner.getValue();
            }
        }
        return val;
    }
}
