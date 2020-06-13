package program.gui;

import program.logic.configs.ProgramConfig;
import program.logic.actions.*;
import program.logic.components.WorldComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static program.gui.FrameUtil.*;

public class MainFrame extends JFrame {
    private static MainFrame mainFrame;
    
    public static MainFrame instance() {
        if (mainFrame == null)
            mainFrame = new MainFrame();
        return mainFrame;
    }

    private static int FRAME_WIDTH = 1000;
    public static int FRAME_HEIGHT = 700;

    private MainFrame() {
        setTitle("WireFrame");
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setCenterScreenLocation(this);
        setLayout(new BorderLayout());

        setJMenuBar(constructMenu());
        add(constructToolBar(), BorderLayout.NORTH);

        add(WorldComponent.instance(), BorderLayout.CENTER);
        WorldComponent.instance().setConfig(new ProgramConfig());
        setVisible(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                FRAME_WIDTH = getWidth();
                FRAME_HEIGHT = getHeight();
                // to create new image with different height and width
                WorldComponent.instance().setConfig(WorldComponent.instance().getConfig());
            }
        });
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

        modify.add(getConfiguredMenuItem(ParametersAction.instance(), "Overall parameters"));
        modify.addSeparator();
        modify.add(getConfiguredMenuItem(BSplineParametersAction.instance(), "BSpline parameters"));
        modify.addSeparator();
        modify.add(getConfiguredMenuItem(AddNewObjectAction.instance(), "Add new Object"));
        modify.add(getConfiguredMenuItem(PickObjectAction.instance(), "Pick object"));

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

        tb.add(getConfiguredButtonToToolbar(ParametersAction.instance(),
                "parameters.png",
                "Overall parameters"));


        tb.add(getConfiguredButtonToToolbar(BSplineParametersAction.instance(),
                "bSplineParameters.png",
                "BSpline Parameters"));

        tb.add(getConfiguredButtonToToolbar(AddNewObjectAction.instance(),
                "addObject.png",
                "Add new Object"));

        tb.add(getConfiguredButtonToToolbar(PickObjectAction.instance(),
                "pickObject.png",
                "Pick object"));

        tb.addSeparator();
        tb.addSeparator();

        tb.add(getConfiguredButtonToToolbar(GetAboutAction.instance(),
                "getAbout.png",
                "Get info"));

        return tb;
    }
}
