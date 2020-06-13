package program.dialogs;

import com.sun.javaws.exceptions.InvalidArgumentException;
import program.Config;
import program.Field;
import program.actions.XorReplaceAction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;

public class OptionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner rowSpinner;
    private JSlider rowSlider;
    private JSpinner columnSpinner;
    private JSpinner lenSpinner;
    private JSpinner widthSpinner;
    private JSlider columnSlider;
    private JSlider lenSlider;
    private JSlider widthSlider;
    private JRadioButton replaceRadioButton;
    private JRadioButton xorRadioButton;
    private JSpinner liveBeginSpinner;
    private JSpinner liveEndSpinner;
    private JSpinner birthBeginSpinner;
    private JSpinner birthEndSpinner;
    private JSpinner firstImpactSpinner;
    private JSpinner secondImpactSpinner;

    private Field field;

    public OptionsDialog(Field field) {
        this.field = field;
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


        setLocationRelativeTo(null);

        Config c = field.getConfig();
        configSpinnerAndIfNotNullSlider(rowSpinner, rowSlider, 1, 200, c.rows);
        configSpinnerAndIfNotNullSlider(columnSpinner, columnSlider, 2, 200, c.columns);
        configSpinnerAndIfNotNullSlider(lenSpinner, lenSlider, 5, 50, c.edgeLength);
        configSpinnerAndIfNotNullSlider(widthSpinner, widthSlider, 1, 20, c.boundWidth);

        configSpinnerAndIfNotNullSlider(liveBeginSpinner, null, 0, 10, c.liveBegin);
        configSpinnerAndIfNotNullSlider(liveEndSpinner, null, 0, 10, c.liveEnd);
        configSpinnerAndIfNotNullSlider(birthBeginSpinner, null, 0, 10, c.birthBegin);
        configSpinnerAndIfNotNullSlider(birthEndSpinner, null, 0, 10, c.birthEnd);

        configSpinnerAndIfNotNullSlider(firstImpactSpinner, null, 0, 10, c.firstImpact);
        configSpinnerAndIfNotNullSlider(secondImpactSpinner, null, 0, 10, c.secondImpact);

        if (c.xorOption) {
            xorRadioButton.setSelected(true);
            replaceRadioButton.setSelected(false);
        } else {
            xorRadioButton.setSelected(false);
            replaceRadioButton.setSelected(true);
        }
        xorRadioButton.addActionListener((event) -> {
            xorRadioButton.setSelected(true);
            replaceRadioButton.setSelected(false);
        });
        replaceRadioButton.addActionListener((event) -> {
            xorRadioButton.setSelected(false);
            replaceRadioButton.setSelected(true);
        });
    }

    private void onOK() {
        Config config = new Config();
        try {
            config.rows = (Integer) rowSpinner.getValue();
            config.columns = (Integer) columnSpinner.getValue();
            config.edgeLength = (Integer) lenSpinner.getValue();
            config.boundWidth = (Integer) widthSpinner.getValue();

            config.xorOption = xorRadioButton.isSelected();

            config.liveBegin = (Double) liveBeginSpinner.getValue();
            config.liveEnd = (Double) liveEndSpinner.getValue();
            config.birthBegin = (Double) birthBeginSpinner.getValue();
            config.birthEnd = (Double) birthEndSpinner.getValue();

            config.firstImpact = (Double) firstImpactSpinner.getValue();
            config.secondImpact = (Double) secondImpactSpinner.getValue();

            config.coloredCells = field.getConfig().coloredCells;

            field.setNewConfig(config);
            if (config.xorOption)
                XorReplaceAction.getInstance().actionPerformed(new ActionEvent(this,0, "xor"));
            else
                XorReplaceAction.getInstance().actionPerformed(new ActionEvent(this,0, "replace"));
        } catch (ClassCastException e) {
            MessageDialog dialog = new MessageDialog("Could not save this configuration");
        } catch(IllegalArgumentException e) {
            MessageDialog dialog = new MessageDialog("Invalid arguments for algorithm");
        }


        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void configSpinnerAndIfNotNullSlider(JSpinner spinner, JSlider slider, int minVal, int maxVal, Number curVal) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        spinner.setValue(curVal);

        if (curVal instanceof Double) {
            spinner.setModel(new SpinnerNumberModel((double)curVal,(double)minVal,(double)maxVal,0.01));
            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    double val = (Double)spinner.getValue();
                    if (val < minVal)
                        val = minVal;
                    if (val > maxVal)
                        val = maxVal;
                    spinner.setValue(val);
                }
            });
        } else if (curVal instanceof Integer) {
            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int val = (Integer)spinner.getValue();
                    if (val < minVal)
                        val = minVal;
                    if (val > maxVal)
                        val = maxVal;
                    spinner.setValue(val);
                    if (slider != null) {
                        slider.setValue(val);
                    }
                }
            });
            if (slider != null) {
                slider.setMinimum(minVal);
                slider.setMaximum(maxVal);
                slider.setValue((Integer)curVal);
                slider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        spinner.setValue(slider.getValue());
                    }
                });
            }
        }
    }
}
