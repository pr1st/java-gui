package program.gui.dialogs;

import program.logic.configs.ProgramConfig;
import program.logic.components.WorldComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class ParametersDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner nSp;
    private JSpinner mSp;
    private JSpinner kSp;
    private JSpinner aSp;
    private JSpinner bSp;
    private JSpinner cSp;
    private JSpinner dSp;
    private JSpinner znSp;
    private JSpinner zfSp;
    private JSpinner swSp;
    private JSpinner shSp;


    private Consumer<ProgramConfig> callback;

    public ParametersDialog(Consumer<ProgramConfig> callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Global parameters");
        setLocationRelativeTo(null);

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

        ProgramConfig c = WorldComponent.instance().getConfig();

        configSpinner(nSp, c.n, 1, 100);
        configSpinner(mSp, c.m, 1, 100);
        configSpinner(kSp, c.k, 1, 100);
        configSpinner(aSp, c.a, 0.0, 1.0);
        configSpinner(bSp, c.b, 0.0, 1.0);
        configSpinner(cSp, c.c, 0.0, 6.29);
        configSpinner(dSp, c.d, 0.0, 6.29);
        configSpinner(znSp, c.zn, 0.0, 1000.0);
        configSpinner(zfSp, c.zf, 0.0, 1000.0);
        configSpinner(swSp, c.sw, 0.0, 100.0);
        configSpinner(shSp, c.sh, 0.0, 100.0);

        pack();
        setVisible(true);
    }

    private void onOK() {
        ProgramConfig c = WorldComponent.instance().getConfig().getCopy();
        c.n = (Integer)nSp.getValue();
        c.m = (Integer)mSp.getValue();
        c.k = (Integer)kSp.getValue();

        c.a = (Double)aSp.getValue();
        c.b = (Double)bSp.getValue();
        c.c = (Double)cSp.getValue();
        c.d = (Double)dSp.getValue();

        c.zn = (Double)znSp.getValue();
        c.zf = (Double)zfSp.getValue();
        c.sw = (Double)swSp.getValue();
        c.sh = (Double)shSp.getValue();

        try {
            c.validateParameters();
            callback.accept(c);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    private void configSpinner(JSpinner spinner, int curVal, int minVal, int maxVal) {
        configUI(spinner);

        spinner.setValue(curVal);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = (Integer) spinner.getValue();
                if (val < minVal)
                    val = minVal;
                if (val > maxVal)
                    val = maxVal;
                spinner.setValue(val);
            }
        });
    }

    private void configSpinner(JSpinner spinner, double curVal, double minVal, double maxVal) {
        configUI(spinner);

        spinner.setModel(new SpinnerNumberModel(curVal,minVal, maxVal, 0.01));
    }

    private void configUI(JSpinner spinner) {
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
