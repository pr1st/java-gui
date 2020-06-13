package program.beans.components;


import program.beans.actions.visualizationOfVolumeDensity.ConfigFile;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

@SingletonBean
public class AbsorptionGraphComponent extends JComponent {


    private ConfigFile.Absorption[] absorptions;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(0,151,401, 151);
        g.drawLine(0,151,0,0);


        g.drawString("0", 0, 164);
        g.drawString("100", 390, 164);
        g.drawString("1.0", 1, 10);


        if (absorptions != null) {
            ConfigFile.Absorption prev = null;
            g.setColor(Color.BLUE);

            for(ConfigFile.Absorption abs: absorptions) {
                if (prev == null) {
                    prev = abs;
                    continue;
                }
                double onePixelY = 150 / 1.0;
                Function<Double, Integer> toGraphY = (d) -> 150 - (int)(onePixelY * d);

                int onePixelX = 400 / 100;
                Function<Integer, Integer> toGraphX = (i) -> 1 + (onePixelX * i);

                g.drawLine(toGraphX.apply(prev.x), toGraphY.apply(prev.value), toGraphX.apply(abs.x), toGraphY.apply(abs.value));
                prev = abs;
            }
        }
    }

    public void setAbsorptions(ConfigFile.Absorption[] absorptions) {
        this.absorptions = absorptions;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(410, 164);
    }
}
