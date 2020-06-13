package program;

import program.actions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Program");
        setSize(900,600);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation();


        Field picture = new Field(new Config());

        JScrollPane scrollPane = new JScrollPane(picture);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        picture.setMainFrame(scrollPane);
        setJMenuBar(constructMenuBar(picture));

        add(constructToolBar(picture), BorderLayout.NORTH);

        XorReplaceAction.getInstance().setField(picture);
        FieldControllerAction.getInstance().setField(picture);

        // picture adds with scroll
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private JMenuBar constructMenuBar(Field field) {
        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("File");


        JMenuItem openFile = new JMenuItem("Open");
        openFile.addActionListener(new OpenFileAction(field));
        openFile.setToolTipText("Open file");
        JMenuItem saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(new SaveFileAction(field));
        saveAs.setToolTipText("Save file");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ExitAction(this));
        exit.setToolTipText("Exit from program");


        file.add(openFile);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);



        JMenu modify = new JMenu("Modify");

        JMenuItem options = new JMenuItem("OptionsDialog");
        options.addActionListener(new OptionAction(field));
        options.setToolTipText("Open options screen");
        JMenuItem xor = new JRadioButtonMenuItem("Xor");
        xor.setSelected(false);
        xor.setActionCommand("xor");
        xor.addActionListener(XorReplaceAction.getInstance());
        XorReplaceAction.getInstance().addXorButton(xor);
        JMenuItem replace = new JRadioButtonMenuItem("Replace");
        replace.setSelected(true);
        replace.setActionCommand("replace");
        replace.addActionListener(XorReplaceAction.getInstance());
        XorReplaceAction.getInstance().addReplaceButton(replace);


        modify.add(options);
        modify.add(xor);
        modify.add(replace);



        JMenu action = new JMenu("Action");


        JMenuItem impact = new JMenuItem("Impact");
        impact.setActionCommand("impact");
        impact.addActionListener(FieldControllerAction.getInstance());
        JMenuItem restart = new JMenuItem("Restart");
        restart.setActionCommand("restart");
        restart.addActionListener(FieldControllerAction.getInstance());
        JMenuItem next = new JMenuItem("Next");
        next.setActionCommand("next");
        next.addActionListener(FieldControllerAction.getInstance());
        JMenuItem run = new JMenuItem("Run");
        run.setActionCommand("run");
        run.addActionListener(FieldControllerAction.getInstance());

        action.add(impact);
        action.add(restart);
        action.add(next);
        action.add(run);

        FieldControllerAction.getInstance().addButtonsToDisableWhileRun(restart);
        FieldControllerAction.getInstance().addButtonsToDisableWhileRun(next);

        JMenu info  = new JMenu("Help");

        JMenuItem aboutProgram = new JMenuItem("About program");
        aboutProgram.addActionListener(new AboutProgramAction());

        info.add(aboutProgram);



        mb.add(file);
        mb.add(modify);
        mb.add(action);
        mb.add(info);

        return mb;
    }

    private JToolBar constructToolBar(Field field) {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton openFile = tb.add(new OpenFileAction(field));
        openFile.setIcon(new ImageIcon("src/main/resources/openFile.png"));
        openFile.setToolTipText("Open file");

        JButton saveFile = tb.add(new SaveFileAction(field));
        saveFile.setIcon(new ImageIcon("src/main/resources/saveFile.png"));
        saveFile.setToolTipText("Save file");

        tb.addSeparator();

        JButton options = tb.add(new OptionAction(field));
        options.setIcon(new ImageIcon("src/main/resources/options.png"));
        options.setToolTipText("Open options screen");


        JToggleButton xor = new JToggleButton();
        tb.add(xor);
        xor.addActionListener(XorReplaceAction.getInstance());
        xor.setSelected(false);
        xor.setActionCommand("xor");
        xor.setToolTipText("Xor");
        xor.setIcon(new ImageIcon("src/main/resources/xor.png"));
        xor.setSelectedIcon(xor.getDisabledIcon());
        XorReplaceAction.getInstance().addXorButton(xor);


        JToggleButton replace = new JToggleButton();
        tb.add(replace);
        replace.addActionListener(XorReplaceAction.getInstance());
        replace.setSelected(true);
        replace.setActionCommand("replace");
        replace.setToolTipText("Replace");
        replace.setIcon(new ImageIcon("src/main/resources/replace.png"));
        replace.setSelectedIcon(replace.getDisabledIcon());
        XorReplaceAction.getInstance().addReplaceButton(replace);

        tb.addSeparator();


        JButton impact = tb.add(FieldControllerAction.getInstance());
        impact.setActionCommand("impact");
        impact.setToolTipText("Impact");
        impact.setIcon(new ImageIcon("src/main/resources/impact.png"));
        JButton restart = tb.add(FieldControllerAction.getInstance());
        restart.setActionCommand("restart");
        restart.setToolTipText("Restart");
        restart.setIcon(new ImageIcon("src/main/resources/restart.png"));
        JButton next = tb.add(FieldControllerAction.getInstance());
        next.setActionCommand("next");
        next.setToolTipText("Next step");
        next.setIcon(new ImageIcon("src/main/resources/nextStep.png"));

        JToggleButton run = new JToggleButton();
        run.addActionListener(FieldControllerAction.getInstance());
        run.setActionCommand("run");
        run.setToolTipText("Run");
        run.setIcon(new ImageIcon("src/main/resources/run.png"));
        tb.add(run);

        FieldControllerAction.getInstance().addButtonsToDisableWhileRun(next);
        FieldControllerAction.getInstance().addButtonsToDisableWhileRun(restart);


        tb.addSeparator();


        JButton aboutProgram = tb.add(new AboutProgramAction());
        aboutProgram.setIcon(new ImageIcon("src/main/resources/aboutProgram.png"));
        aboutProgram.setToolTipText("info");

        return tb;
    }

    private void setLocation() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        //Calculate the frame location
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        //Set the new frame location
        setLocation(x, y);
    }

}
