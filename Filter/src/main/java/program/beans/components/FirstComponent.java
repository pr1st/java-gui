package program.beans.components;

import program.iocContainer.SingletonBean;

import java.awt.*;
import java.awt.image.BufferedImage;

@SingletonBean
public class FirstComponent extends AbstractJComponent {
    private Dimension scaledDimensionSize;

    private BufferedImage boundsImage;
    private Rectangle selectedBounds;

    @Override
    protected void paintComponent(Graphics g) {
        if (scaledDimensionSize == null)
            scaledDimensionSize = new Dimension(350,350);
        g.drawImage(image,1,1,scaledDimensionSize.width, scaledDimensionSize.height,this);
        if (selectedBounds != null) {
            g.setPaintMode();
            if (boundsImage == null)
                boundsImage = createDottedBoarderImage(selectedBounds.width + 2, selectedBounds.height + 2);
            g.drawImage(boundsImage, selectedBounds.x, selectedBounds.y, this);
        }
    }

    @Override
    public void setImage(BufferedImage image) {
        scaledDimensionSize = getScaledDimension(new Dimension(image.getWidth(),image.getHeight()), new Dimension(350,350));
        boundsImage = null;
        selectedBounds = null;
        super.setImage(image);
    }

    public BufferedImage selectArea(int x, int y) {
        Point topLeft = getTopLeftCorner(x * image.getWidth() / scaledDimensionSize.width, y * image.getHeight() / scaledDimensionSize.height);
        Dimension bounds = getSizeOfBoundImage(topLeft.x, topLeft.y);
        selectedBounds = new Rectangle(
                topLeft.x * scaledDimensionSize.width / image.getWidth(),
                topLeft.y * scaledDimensionSize.height / image.getHeight(),
                bounds.width * scaledDimensionSize.width / image.getWidth() + ((image.getWidth() > 350) ? 1 : 0),
                bounds.height * scaledDimensionSize.height / image.getHeight() + ((image.getHeight() > 350) ? 1 : 0));
        repaint();
        return image.getSubimage(topLeft.x,topLeft.y, bounds.width, bounds.height);
    }

    private Point getTopLeftCorner(int centerX, int centerY) {
        int resX = centerX - 350/2;
        int resY = centerY - 350/2;
        if(resX < 0)
            resX = 0;
        if(resY < 0)
            resY = 0;
        if (resX + 350 > image.getWidth())
            resX = image.getWidth() - 350;
        if (resY + 350 > image.getHeight())
            resY = image.getHeight() - 350;
        if(resX < 0)
            resX = 0;
        if(resY < 0)
            resY = 0;
        return new Point(resX, resY);
    }

    private Dimension getSizeOfBoundImage(int topLeftX, int topLeftY) {
        int resW = 350;
        int resH = 350;
        if (topLeftX + resW > image.getWidth())
            resW = image.getWidth();
        if (topLeftY + resH > image.getHeight())
            resH = image.getHeight();
        return new Dimension(resW, resH);
    }


    private static BufferedImage createDottedBoarderImage(int width, int height) {
        BufferedImage im = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();


        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        int delta = 0;
        int nextDelta = 10;
        while (true) {
            if (nextDelta > width - 1)
                break;
            g.drawLine(delta, 0, nextDelta, 0);
            g.drawLine(delta, height - 1, nextDelta, height - 1);

            delta = nextDelta + 10;
            nextDelta += 20;
        }
        delta = 0;
        nextDelta = 10;
        while (true) {
            if (nextDelta > height - 1)
                break;
            g.drawLine(0, delta, 0, nextDelta);
            g.drawLine(width - 1, delta, width - 1, nextDelta);

            delta = nextDelta + 10;
            nextDelta += 20;
        }
        return im;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int originalW = imgSize.width;
        int originalH = imgSize.height;
        int maxW = boundary.width;
        int maxH = boundary.height;
        int resultingW = originalW;
        int resultingH  = originalH;


        if (originalW <= maxW && originalH <= maxH) {
            return new Dimension(resultingW, resultingH);
        }

        if (originalW < originalH) {
            resultingH = maxH;
            resultingW = (originalW * maxH) / originalH;
        } else {
            resultingW = maxW;
            resultingH = (originalH * maxW) / originalW;
        }

        return new Dimension(resultingW, resultingH);
    }
}
