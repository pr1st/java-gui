package program.gui.dialogs;

import program.logic.components.WorldComponent;
import program.logic.configs.BSplineConfig;
import program.logic.components.BSplineComponent;
import program.logic.configs.ProgramConfig;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

public class BSplineParametersDialog extends JDialog {
    private JSpinner rSpinner;
    private JSpinner gSpinner;
    private JSpinner bSpinner;


    private BSplineConfig oldConfig;
    private BSplineComponent spline;
    private boolean isChanged = false;

    private static final int PARAMETERS_FRAME_WIDTH = 600;
    private static final int PARAMETERS_FRAME_HEIGHT = 750;

    public BSplineParametersDialog(BSplineConfig config) {
        setTitle("BSpline Parameters");
        setSize(PARAMETERS_FRAME_WIDTH, PARAMETERS_FRAME_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        oldConfig = config.getCopy();
        spline = new BSplineComponent(PARAMETERS_FRAME_WIDTH, PARAMETERS_FRAME_HEIGHT - 150, config);
        main.add(spline, BorderLayout.CENTER);
        main.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);


        JPanel params = new JPanel();
        params.setLayout(new GridLayout(2, 4));


        rSpinner = configColorSpinner(config.color.getRed());
        gSpinner = configColorSpinner(config.color.getGreen());
        bSpinner = configColorSpinner(config.color.getBlue());


        JLabel label = new JLabel("COLOR:  ");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        params.add(label);
        params.add(rSpinner);
        params.add(gSpinner);
        params.add(bSpinner);


        JButton addPoint = new JButton("Add point");
        JButton removePoint = new JButton("Remove point");
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        addPoint.addActionListener(this::onAddPoint);
        removePoint.addActionListener(this::onRemovePoint);
        ok.addActionListener(this::onOk);
        cancel.addActionListener(this::onCancel);

        params.add(addPoint);
        params.add(removePoint);
        params.add(ok);
        params.add(cancel);

        add(params, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private JSpinner configColorSpinner(int startingValue) {
        JSpinner spinner = new JSpinner();
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        spinner.setValue(startingValue);


        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = (Integer) spinner.getValue();
                if (val < 0)
                    val = 0;
                if (val > 255)
                    val = 255;
                spinner.setValue(val);

                BSplineConfig config = spline.getConfig().getCopy();
                config.color = new Color((Integer) rSpinner.getValue(), (Integer)gSpinner.getValue(),(Integer)bSpinner.getValue());
                try {
                    config.validateParameters();
                    spline.setConfig(config);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    Color saved = spline.getConfig().color;
                    rSpinner.setValue(saved.getRed());
                    gSpinner.setValue(saved.getGreen());
                    bSpinner.setValue(saved.getBlue());
                }
            }
        });
        return spinner;
    }

    private void onOk(ActionEvent e) {
        isChanged = true;
        ProgramConfig c = WorldComponent.instance().getConfig();
        c.elements.set(c.selectedElementId, spline.getConfig());
        WorldComponent.instance().setConfig(c);
        dispose();
    }

    private void onCancel(ActionEvent e) {
        isChanged = false;
        spline.setConfig(oldConfig);
        dispose();
    }

    private void onAddPoint(ActionEvent e) {
        new AddPointInBSplineDialog(this::addPointToImage, spline.getConfig().points.size());
    }

    private void onRemovePoint(ActionEvent e) {
        if (spline.getConfig().points.size() > 4)
            new RemovePointFromBSplineDialog(this::removePointToImage, spline.getConfig().points);
        else
            JOptionPane.showMessageDialog(this, "Cant be fewer than 4 points", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addPointToImage(Point2D point, Integer index) {
        BSplineConfig config = spline.getConfig();
        config.points.add(index, point);
        spline.setConfig(config);
    }

    private void removePointToImage(Point2D point) {
        BSplineConfig config = spline.getConfig();
        config.points.remove(point);
        spline.setConfig(config);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public BSplineConfig getBSpline() {
        return spline.getConfig();
    }
}
