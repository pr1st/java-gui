package program.beans.components;

import program.beans.utilBeans.DottedBorder;
import program.iocContainer.Inject;
import program.iocContainer.PostConstruct;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AbstractJComponent extends JComponent {


    protected BufferedImage image;

    @Inject
    DottedBorder border;

    @PostConstruct
    public void construct() {
        setBorder(border);
        restoreImage();
    }

    public void restoreImage() {
        image = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0,0,image.getWidth(),image.getHeight());
        setImage(image);
    }


    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,1,1,this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(352,352);
    }


    @Override
    public Dimension getMaximumSize(){
        return getPreferredSize();
    }
    @Override
    public Dimension getMinimumSize(){
        return getPreferredSize();
    }
}
