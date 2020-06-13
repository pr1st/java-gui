package program.logic.components;

import program.logic.Configuration;
import program.logic.actions.businessActions.ShowIsolinesAction;
import program.logic.actions.businessActions.ShowMapAction;
import program.logic.actions.businessActions.UseInterpolationAction;
import program.logic.images.FieldImage;
import program.logic.images.InterpolatedFieldImage;
import program.logic.images.IsolineImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class LegendForField extends JComponent {
    private static LegendForField legendForField;

    public static LegendForField instance() {
        if (legendForField == null)
            legendForField = new LegendForField();
        return legendForField;
    }

    private LegendForField() {
    }

    private Configuration configuration;
    private InterpolatedFieldImage interpolatedLegend;
    private FieldImage legend;
    private List<IsolineImage> legendIsolines;
    private BufferedImage values;

    private final int LEGEND_WIDTH = 100;
    private final int LEGEND_HEIGHT  = 500;

    private BiFunction<Double, Double, Double> f;

    public void setupLegend(MainField field) {
        this.configuration = field.getConfiguration();
        this.f = (x, y) -> {
            return configuration.min + ((y - configuration.c) / (configuration.d - configuration.c)) * (configuration.max - configuration.min);
        };
        setupField();
        setupIsolines();
        setupValues();
    }

    private void setupField() {
        legend = new FieldImage(LEGEND_WIDTH , LEGEND_HEIGHT, f);
        legend.drawImage(configuration);
        interpolatedLegend = new InterpolatedFieldImage(LEGEND_WIDTH , LEGEND_HEIGHT, f);
        interpolatedLegend.drawImage(configuration);
    }


    private void setupIsolines() {
        legendIsolines = new ArrayList<>();
        for (double z: configuration.isolineValues) {
            IsolineImage isoline = new IsolineImage(LEGEND_WIDTH, LEGEND_HEIGHT, f);
            isoline.drawImage(configuration, z);
            legendIsolines.add(isoline);
        }
    }

    private void setupValues() {
        DecimalFormat formatter = new DecimalFormat("#.#");
        values = new BufferedImage(70, LEGEND_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = values.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0,0, values.getWidth(), values.getHeight());
        g.setColor(Color.BLACK);

        g.drawLine(0,0,0,values.getHeight() - 1);

        g.drawString(formatter.format(configuration.max), 3, 10);
        g.drawString(formatter.format(configuration.min), 3, values.getHeight() - 1);


        int step = values.getHeight() / (configuration.n +1);
        for(int i = 0; i < configuration.isolineValues.size(); i++) {
            g.drawString(formatter.format(configuration.isolineValues.get(i)), 3, (configuration.n - i)* step + 3);
        }

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(0,0, getWidth() - 1, getHeight() - 1);

        g.drawImage(values, LEGEND_WIDTH + 1, 1, this);

        if (ShowMapAction.instance().isActivated()) {
            if (UseInterpolationAction.instance().isActivated()) {
                g.drawImage(interpolatedLegend,1, 1, this);
            } else {
                g.drawImage(legend, 1, 1, this);
            }
        }

        if (ShowIsolinesAction.instance().isActivated()) {
            for (IsolineImage isoline : legendIsolines) {
                g.drawImage(isoline, 1, 1, this);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(LEGEND_WIDTH + 70 + 2, LEGEND_HEIGHT + 2);
    }
}
