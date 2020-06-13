package program.logic.components;

import program.MainFunction;
import program.logic.Configuration;
import program.logic.ProgramUtil;
import program.logic.actions.businessActions.*;
import program.logic.images.FieldImage;
import program.logic.images.GridImage;
import program.logic.images.InterpolatedFieldImage;
import program.logic.images.IsolineImage;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class MainField extends JComponent {
    private static MainField mainField;

    public static MainField instance() {
        if (mainField == null)
            mainField = new MainField();
        return mainField;
    }

    public final int FIELD_WIDTH = 700;
    public final int FIELD_HEIGHT = 500;

    private Configuration configuration;

    private FieldImage field;
    private InterpolatedFieldImage interpolatedField;
    private GridImage grid;

    private List<IsolineImage> mapIsolines;
    private List<IsolineImage> userIsolines;
    private IsolineImage dynamicIsoline;

    private MainField() {
        field = new FieldImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);
        interpolatedField = new InterpolatedFieldImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);
        grid = new GridImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);


        mapIsolines = new ArrayList<>();
        userIsolines = new ArrayList<>();
    }


    public void setupField(Configuration configuration) {
        this.configuration = configuration;
        configuration.setIsolineValues(field.getWidth(), field.getHeight());

        setupFieldImage();
        setupIsolineImages();
        setupGridImage();

    }


    private void setupFieldImage() {
        field.drawImage(configuration);
        interpolatedField.drawImage(configuration);
    }

    private void setupGridImage() {
        grid.drawImage(configuration);
    }

    private void setupIsolineImages() {
        mapIsolines.clear();
        userIsolines.clear();
        dynamicIsoline = null;
        for (double z: configuration.isolineValues) {
            IsolineImage isoline = new IsolineImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);
            isoline.drawImage(configuration, z);
            mapIsolines.add(isoline);
        }
    }

    public void createUserIsoline(double z) {
        IsolineImage isoline = new IsolineImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);
        isoline.drawImage(configuration, z);
        userIsolines.add(isoline);
    }

    public void clearUserIsolines() {
        userIsolines.clear();
        dynamicIsoline = null;
    }

    public void moveDynamicIsoline(double z) {
        dynamicIsoline = new IsolineImage(FIELD_WIDTH, FIELD_HEIGHT, MainFunction::f);
        dynamicIsoline.drawImage(configuration, z);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(0,0, field.getWidth() + 1, field.getHeight() + 1);

        g.setPaintMode();

        if (ShowMapAction.instance().isActivated()) {
            if (UseInterpolationAction.instance().isActivated()) {
                g.drawImage(interpolatedField,1,1,this);
            } else {
                g.drawImage(field, 1, 1, this);
            }
        }

        if (ShowGridAction.instance().isActivated()) {
            g.drawImage(grid, 1, 1, this);
        }


        if(ShowIsolinesAction.instance().isActivated()) {
            Stream.concat(
                    Stream.concat(mapIsolines.stream(), userIsolines.stream()),
                    dynamicIsoline != null ? Stream.of(dynamicIsoline) : Stream.empty())
                    .forEach( isoline -> {
                        g.drawImage(isoline, 1, 1, this);
                        if (ShowIsolinePointsAction.instance().isActivated())
                            g.drawImage(isoline.getPointsImage(), 1, 1, this);
                    });
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(field.getWidth() + 2, field.getHeight() + 2);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
