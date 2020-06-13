package program.gui.dialogs;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.function.BiConsumer;

public class AddPointInBSplineDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinnerX;
    private JSpinner spinnerY;
    private JSpinner spinnerK;
    private JLabel overAllPoints;
    private JLabel hintToK;


    private BiConsumer<Point2D, Integer> callback;
    AddPointInBSplineDialog(BiConsumer<Point2D, Integer> callback, int numberOfPoints) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        setTitle("Add point");
        overAllPoints.setText("Overall points: " + numberOfPoints);
        hintToK.setText("(K: index of created point)");

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


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        configPointSpinner(spinnerX);
        configPointSpinner(spinnerY);

        spinnerK.setModel(new SpinnerNumberModel(numberOfPoints,0,numberOfPoints,1));
        spinnerK.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        pack();
        setVisible(true);
    }

    private void onOK() {
        callback.accept(new Point2D.Double((Double)spinnerX.getValue(), (Double)spinnerY.getValue()), (Integer)spinnerK.getValue());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void configPointSpinner(JSpinner spinner) {
        spinner.setModel(new SpinnerNumberModel(0.0,-100.0,100.0,0.01));
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });
    }
}
