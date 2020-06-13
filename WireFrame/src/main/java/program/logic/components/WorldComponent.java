package program.logic.components;

import program.gui.MainFrame;
import program.logic.actions.MovementAction;
import program.logic.configs.ProgramConfig;
import program.logic.images.WorldImage;

import javax.swing.*;
import java.awt.*;

public class WorldComponent extends JComponent {
    private static WorldComponent worldComponent;

    public static WorldComponent instance() {
        if (worldComponent == null)
            worldComponent = new WorldComponent();
        return worldComponent;
    }

    private WorldComponent() {
        addMouseMotionListener(MovementAction.instance());
        addMouseWheelListener(MovementAction.instance());
        addMouseListener(MovementAction.instance());
    }

    private ProgramConfig config;
    private WorldImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }


    public void setConfig(ProgramConfig config) {
        this.config = config;
        setSize((int)(config.sw/ config.sh * (MainFrame.FRAME_HEIGHT - 150)), MainFrame.FRAME_HEIGHT - 150);

        image = new WorldImage(getWidth(), getHeight());
        image.setConfig(config);
        repaint();
    }

    public ProgramConfig getConfig() {
        return config;
    }
}
