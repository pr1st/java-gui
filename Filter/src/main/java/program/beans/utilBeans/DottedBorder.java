package program.beans.utilBeans;


import program.iocContainer.SingletonBean;

import javax.swing.border.AbstractBorder;
import java.awt.*;


@SingletonBean
public class DottedBorder extends AbstractBorder {

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y
            , int width
            , int height)
    {
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d = null;
        if (g instanceof Graphics2D)
        {
            g = (Graphics2D)g;
            int delta = 0;
            int nextDelta = 10;
            while (true) {
                if (x + nextDelta > width - 1)
                    break;
                g.drawLine(x + delta, y, x + nextDelta ,y);
                g.drawLine(x + delta, height - 1, x + nextDelta ,height - 1);

                delta = nextDelta + 10;
                nextDelta += 20;
            }
            delta = 0;
            nextDelta = 10;
            while (true) {
                if (y + nextDelta > height - 1)
                    break;
                g.drawLine(x, y +delta, x,y + nextDelta);
                g.drawLine(width - 1, y +delta, width - 1,y + nextDelta);

                delta = nextDelta + 10;
                nextDelta += 20;
            }
        }
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
