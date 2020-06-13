package program.beans.components;


import program.beans.actions.visualizationOfVolumeDensity.ConfigFile;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

@SingletonBean
public class EmissionGraphComponent extends JComponent {

    private ConfigFile.Emission[] emissions;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(0,153,401, 153);
        g.drawLine(0,151,0,0);


        g.drawString("0", 0, 164);
        g.drawString("100", 390, 164);
        g.drawString("255", 0, 10);

        if (emissions != null) {
            ConfigFile.Emission prev = null;
            for(ConfigFile.Emission emis: emissions) {
                if (prev == null) {
                    prev = emis;
                    continue;
                }

                double onePixelY = 150 / 255.0;
                Function<Integer, Integer> toGraphY = (d) -> 152 - (int)(onePixelY * d);

                int onePixelX = 400 / 100;
                Function<Integer, Integer> toGraphX = (i) -> 1 + (onePixelX * i);


                g.setColor(Color.RED);
                g.drawLine(
                        toGraphX.apply(prev.x),
                        toGraphY.apply(prev.color.getRed()),
                        toGraphX.apply(emis.x),
                        toGraphY.apply(emis.color.getRed())
                );

                g.setColor(Color.GREEN);
                g.drawLine(
                        toGraphX.apply(prev.x) + 1,
                        toGraphY.apply(prev.color.getGreen()) - 1,
                        toGraphX.apply(emis.x) + 1,
                        toGraphY.apply(emis.color.getGreen()) - 1
                );


                g.setColor(Color.BLUE);
                g.drawLine(toGraphX.apply(prev.x) + 2,
                        toGraphY.apply(prev.color.getBlue()) - 2,
                        toGraphX.apply(emis.x) + 2,
                        toGraphY.apply(emis.color.getBlue()) - 2);

                prev = emis;
            }
        }
    }

    public void setEmissions(ConfigFile.Emission[] emissions) {
        this.emissions = emissions;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(410, 164);
    }

}
