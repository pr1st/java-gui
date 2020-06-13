package program.logic.components;

import program.logic.configs.BSplineConfig;
import program.logic.actions.BSplineMouseAction;
import program.logic.images.BSplineImage;

import javax.swing.*;
import java.awt.*;

public class BSplineComponent extends JComponent {

    private BSplineConfig config;
    private BSplineImage image;

    public BSplineComponent(int width, int height, BSplineConfig config) {
        setSize(width, height);
        setConfig(config);

        BSplineMouseAction action = new BSplineMouseAction(this);
        addMouseListener(action);
        addMouseMotionListener(action);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public void setConfig(BSplineConfig config) {
        config.reCalculateMaxRange();
        this.config = config;

        image = new BSplineImage(getWidth(), getHeight());
        image.setConfig(config);
        repaint();
    }

    public BSplineImage getImage() {
        return image;
    }

    public BSplineConfig getConfig() {
        return config;
    }
}
