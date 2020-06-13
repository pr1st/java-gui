package program.gui;

import program.logic.Configuration;
import program.logic.actions.ToggleAction;
import program.logic.actions.businessActions.*;
import program.logic.actions.generalActions.ExitAction;
import program.logic.actions.generalActions.GetAboutAction;
import program.logic.actions.generalActions.OpenFileAction;
import program.logic.actions.generalActions.OptionsAction;
import program.logic.components.LegendForField;
import program.logic.components.MainField;
import program.logic.components.StatusBar;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame mainFrame;

    public static MainFrame instance() {
        if (mainFrame == null)
            mainFrame = new MainFrame();
        return mainFrame;
    }

    private MainFrame() {
        setTitle("Isolines");
        setSize(1200,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation();
        setLayout(new BorderLayout());

        setJMenuBar(constructMenu());
        add(constructToolBar(), BorderLayout.NORTH);
        ShowIsolinesAction.instance().actionPerformed(null);
        ShowMapAction.instance().actionPerformed(null);



        JPanel main = new JPanel();

        main.add(MainField.instance());
        MainField.instance().setupField(Configuration.getDefault());
        MainField.instance().addMouseListener(MouseControllerAction.instance());
        MainField.instance().addMouseMotionListener(MouseControllerAction.instance());
        main.add(LegendForField.instance());
        LegendForField.instance().setupLegend(MainField.instance());

        MouseControllerAction.instance().setupController(Configuration.getDefault());

        add(main, BorderLayout.CENTER);

        add(StatusBar.instance(), BorderLayout.SOUTH);
    }


    private JMenuBar constructMenu() {
        JMenuBar mb = new JMenuBar();
        mb.add(constructFileMenu());
        mb.add(constructModifyMenu());
        mb.add(constructInfoMenu());
        return mb;
    }


    private JMenu constructFileMenu() {
        JMenu file = new JMenu("File");
        file.add(getConfiguredMenuItem(OpenFileAction.instance(), "Open file"));
        file.addSeparator();
        file.add(getConfiguredMenuItem(ExitAction.instance(), "Exit"));
        return file;
    }

    private JMenu constructModifyMenu() {
        JMenu modify = new JMenu("Modify");

        modify.add(getConfiguredMenuItem(OptionsAction.instance(), "Options"));
        modify.addSeparator();
        modify.add(getConfiguredRadioMenuItem(UseInterpolationAction.instance(), "Use color interpolation"));
        modify.add(getConfiguredRadioMenuItem(ShowGridAction.instance(), "Show grid"));
        modify.add(getConfiguredRadioMenuItem(ShowIsolinesAction.instance(), "Show isolines"));
        modify.add(getConfiguredRadioMenuItem(ShowIsolinePointsAction.instance(), "Show isoline points"));
        modify.add(getConfiguredRadioMenuItem(ShowMapAction.instance(), "Show map"));
        modify.add(getConfiguredMenuItem(ClearIsolinesAction.instance(), "Clear user isolines"));

        return modify;
    }

    private JMenu constructInfoMenu() {
        JMenu info = new JMenu("Info");
        info.add(getConfiguredMenuItem(GetAboutAction.instance(), "About"));
        return info;
    }

    private JToolBar constructToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);


        tb.add(getConfiguredButtonToToolbar(OpenFileAction.instance(),
                "openFile.png",
                "Open file"));

        tb.addSeparator();
        tb.addSeparator();

        tb.add(getConfiguredButtonToToolbar(OptionsAction.instance(),
                "options.png",
                "Options"));

        tb.addSeparator();

        tb.add(getConfiguredToggleButtonToToolbar(UseInterpolationAction.instance(),
                "colorInterpolation.png",
                "Use color interpolation"));

        tb.add(getConfiguredToggleButtonToToolbar(ShowGridAction.instance(),
                "showGrid.png",
                "Show grid"));


        tb.add(getConfiguredToggleButtonToToolbar(ShowIsolinesAction.instance(),
                "showIsolines.png",
                "Show isolines"));

        tb.add(getConfiguredToggleButtonToToolbar(ShowIsolinePointsAction.instance(),
                "showIsolinePoints.png",
                "Show isoline points"));


        tb.add(getConfiguredToggleButtonToToolbar(ShowMapAction.instance(),
                "showMap.png",
                "Show map"));


        tb.add(getConfiguredButtonToToolbar(ClearIsolinesAction.instance(),
                "clearUserIsolines.png",
                "Clear user isolines"));

        tb.addSeparator();
        tb.addSeparator();

        tb.add(getConfiguredButtonToToolbar(GetAboutAction.instance(),
                "getAbout.png",
                "Get info"));

        return tb;
    }

    private void setLocation() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        //Calculate the frame location
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        //Set the new frame location
        setLocation(x, y - 20);
    }

    private static JButton getConfiguredButtonToToolbar(AbstractAction action, String iconName, String toolTipText) {
        JButton button = new JButton();
        button.addActionListener(action);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/" + iconName).getPath()));
        button.setToolTipText(toolTipText);
        return button;
    }

    private static JToggleButton getConfiguredToggleButtonToToolbar(ToggleAction action, String iconName, String toolTipText) {
        JToggleButton button = new JToggleButton();
        button.addActionListener(action);
        button.setBorder(BorderFactory.createEtchedBorder());
        action.addToggleButton(button);
        button.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/" + iconName).getPath()));
        button.setToolTipText(toolTipText);
        return button;
    }

    private static JMenuItem getConfiguredMenuItem(AbstractAction action, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }

    private static JRadioButtonMenuItem getConfiguredRadioMenuItem(ToggleAction action, String name) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
        item.addActionListener(action);
        action.addToggleButton(item);
        return item;
    }

}
