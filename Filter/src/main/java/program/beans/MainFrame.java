package program.beans;


import program.beans.actions.configActions.*;
import program.beans.actions.pictureChangers.*;
import program.beans.actions.visualizationOfVolumeDensity.EnableAbsorptionAction;
import program.beans.actions.visualizationOfVolumeDensity.EnableEmissionAction;
import program.beans.actions.visualizationOfVolumeDensity.OpenConfigurationAction;
import program.beans.actions.visualizationOfVolumeDensity.RunRenderAction;
import program.beans.components.*;
import program.beans.utilBeans.SplitComponent;
import program.iocContainer.Inject;
import program.iocContainer.PostConstruct;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;


@SingletonBean
public class MainFrame extends JFrame {

    // components
    @Inject
    private FirstComponent firstComponent;
    @Inject
    private SecondComponent secondComponent;
    @Inject
    private ThirdComponent thirdComponent;

    // volume visualization components
    @Inject
    AbsorptionGraphComponent absorptionGraphComponent;
    @Inject
    EmissionGraphComponent emissionGraphComponent;



    // config actions
    @Inject
    private NewDocAction newDocAction;
    @Inject
    private LoadImageAction loadImageAction;
    @Inject
    private SaveImageAction saveImageAction;
    @Inject
    private SelectImageAction selectImageAction;
    @Inject
    private GetAboutAction getAboutAction;
    @Inject
    private ExitAction exitAction;
    @Inject
    private CopyCToBImageAction copyCToBImageAction;


    // modify image actions
    @Inject
    private CopyBToCImageAction copyBToCImageAction;
    @Inject
    private ToBlackAndWhiteAction toBlackAndWhiteAction;
    @Inject
    private NegativeAction negativeAction;
    @Inject
    private FloydDitheringAction floydDitheringAction;
    @Inject
    private OrderedDitheringAction orderedDitheringAction;
    @Inject
    private DoubleImageAction doubleImageAction;
    @Inject
    private FindEdgesRobertAction findEdgesRobertAction;
    @Inject
    private FindEdgesSobelAction findEdgesSobelAction;
    @Inject
    private BlurFilterAction blurFilterAction;
    @Inject
    private SharpenFilterAction sharpenFilterAction;
    @Inject
    private EmbossAction embossAction;
    @Inject
    private WaterColorAction waterColorAction;
    @Inject
    private TurnImageAction turnImageAction;
    @Inject
    private GammaCorrectionAction gammaCorrectionAction;


    // visualization of volume rendering
    @Inject
    private OpenConfigurationAction openConfigurationAction;
    @Inject
    private EnableAbsorptionAction enableAbsorptionAction;
    @Inject
    private EnableEmissionAction enableEmissionAction;
    @Inject
    private RunRenderAction runRenderAction;


    @PostConstruct
    void construct() {
        setTitle("Filter");
        setSize(1200,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation();
        setLayout(new BorderLayout());


        setJMenuBar(constructMenu());

        JPanel toolbars = new JPanel(new GridLayout(3, 1));
        toolbars.add(constructConfigToolBar());
        toolbars.add(constructPictureChangerToolBar());
        toolbars.add(constructVisualizationOfVolumeDensityToolBar());

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));

        main.add(new SplitComponent());
        main.add(firstComponent);
        main.add(new SplitComponent());
        main.add(secondComponent);
        main.add(new SplitComponent());
        main.add(thirdComponent);


        JPanel graphs = new JPanel();

        graphs.add(absorptionGraphComponent);
        graphs.add(emissionGraphComponent);

        add(toolbars, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
        add(graphs, BorderLayout.SOUTH);
    }

    private JMenuBar constructMenu() {
        JMenuBar mb = new JMenuBar();

        mb.add(configFileMenu());
        mb.add(configModifySecondZoneMenu());
        mb.add(configModifyThirdZoneMenu());
        mb.add(configVolumeVisualizationMenu());
        mb.add(configInfoMenu());

        return mb;
    }

    private JMenu configFileMenu() {
        JMenu file = new JMenu("File");

        file.add(getConfiguredMenuItem(newDocAction, "New document"));
        file.add(getConfiguredMenuItem(loadImageAction, "Load image"));
        file.add(getConfiguredMenuItem(saveImageAction, "Save as"));
        file.addSeparator();
        file.add(getConfiguredMenuItem(exitAction, "Exit"));

        return file;
    }

    private JMenu configModifySecondZoneMenu() {
        JMenu modifySecondZone = new JMenu("Second zone");
        modifySecondZone.add(getConfiguredRadioMenuItem(selectImageAction, "Select", selectImageAction::addToggleButton));
        modifySecondZone.add(getConfiguredMenuItem(copyCToBImageAction, "Copy from third"));
        return modifySecondZone;
    }

    private JMenu configModifyThirdZoneMenu() {
        JMenu modifyThirdZone = new JMenu("Third zone");

        modifyThirdZone.add(getConfiguredMenuItem(copyBToCImageAction, "Copy from second"));
        modifyThirdZone.add(getConfiguredMenuItem(toBlackAndWhiteAction, "To black and white"));
        modifyThirdZone.add(getConfiguredMenuItem(negativeAction, "Negative"));
        modifyThirdZone.addSeparator();


        JMenu dithering = new JMenu("Dithering");
        dithering.add(getConfiguredMenuItem(floydDitheringAction, "Floyd dithering"));
        dithering.add(getConfiguredMenuItem(orderedDitheringAction, "Ordered dithering"));
        modifyThirdZone.add(dithering);

        modifyThirdZone.addSeparator();
        modifyThirdZone.add(getConfiguredMenuItem(doubleImageAction, "Double image"));
        modifyThirdZone.addSeparator();

        JMenu findEdges = new JMenu("Find edges");
        findEdges.add(getConfiguredMenuItem(findEdgesRobertAction, "Find edges by Robert"));
        findEdges.add(getConfiguredMenuItem(findEdgesSobelAction, "Find edges by Sobel"));
        modifyThirdZone.add(findEdges);

        modifyThirdZone.addSeparator();
        modifyThirdZone.add(getConfiguredMenuItem(blurFilterAction, "Blur filter"));
        modifyThirdZone.add(getConfiguredMenuItem(sharpenFilterAction, "Sharpen filter"));
        modifyThirdZone.add(getConfiguredMenuItem(embossAction, "Emboss"));
        modifyThirdZone.add(getConfiguredMenuItem(waterColorAction, "Water color"));
        modifyThirdZone.addSeparator();
        modifyThirdZone.add(getConfiguredMenuItem(turnImageAction, "Turn image"));
        modifyThirdZone.addSeparator();
        modifyThirdZone.add(getConfiguredMenuItem(gammaCorrectionAction, "Gamma correction"));

        return modifyThirdZone;
    }

    private JMenu configVolumeVisualizationMenu() {
        JMenu volumeVisualization = new JMenu("Volume visualization");

        volumeVisualization.add(getConfiguredMenuItem(openConfigurationAction, "Open configuration"));
        volumeVisualization.add(getConfiguredRadioMenuItem(enableAbsorptionAction, "Enable absorption", enableAbsorptionAction::addToggleButton));
        volumeVisualization.add(getConfiguredRadioMenuItem(enableEmissionAction, "Enable emission", enableEmissionAction::addToggleButton));
        volumeVisualization.add(getConfiguredMenuItem(runRenderAction, "Run rendering"));

        return volumeVisualization;
    }

    private JMenu configInfoMenu() {
        JMenu info  = new JMenu("Help");
        info.add(getConfiguredMenuItem(getAboutAction, "About program"));
        return info;
    }


    private JToolBar constructConfigToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        tb.add(getConfiguredButtonToToolbar(newDocAction,
                "src/main/resources/utilIcons/newDocument.png",
                "New document"));

        tb.add(getConfiguredButtonToToolbar(loadImageAction,
                "src/main/resources/utilIcons/loadImage.png",
                "Load image"));

        tb.add(getConfiguredButtonToToolbar(saveImageAction,
                "src/main/resources/utilIcons/saveFile.png",
                "Save image"));


        tb.addSeparator();


        tb.add(getConfiguredToggleButtonToToolbar(selectImageAction,
                "src/main/resources/utilIcons/select.png",
                "Select",
                selectImageAction::addToggleButton
        ));

        tb.add(getConfiguredButtonToToolbar(copyCToBImageAction,
                "src/main/resources/utilIcons/copyFromCtoB.png",
                "Copy from C to B"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(getAboutAction,
                "src/main/resources/utilIcons/aboutProgram.png",
                "Info"));

        return tb;
    }

    private JToolBar constructPictureChangerToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);


        tb.add(getConfiguredButtonToToolbar(copyBToCImageAction,
                "src/main/resources/changeImageIcons/copyFromBtoC.png",
                "Copy from B to C"));

        tb.add(getConfiguredButtonToToolbar(toBlackAndWhiteAction,
                "src/main/resources/changeImageIcons/toBlackAndWhite.png",
                "To black and white"));

        tb.add(getConfiguredButtonToToolbar(negativeAction,
                "src/main/resources/changeImageIcons/negative.png",
                "Negative"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(floydDitheringAction,
                "src/main/resources/changeImageIcons/floydDithering.png",
                "Floyd dithering"));

        tb.add(getConfiguredButtonToToolbar(orderedDitheringAction,
                "src/main/resources/changeImageIcons/orderedDithering.png",
                "Ordered dithering"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(doubleImageAction,
                "src/main/resources/changeImageIcons/doubleImage.png",
                "Double image"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(findEdgesRobertAction,
                "src/main/resources/changeImageIcons/findEdgesRobert.png",
                "Find edges Robert"));


        tb.add(getConfiguredButtonToToolbar(findEdgesSobelAction,
                "src/main/resources/changeImageIcons/findEdgesSobel.png",
                "Find edges Sobel"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(blurFilterAction,
                "src/main/resources/changeImageIcons/blurFilter.png",
                "Blur filter"));

        tb.add(getConfiguredButtonToToolbar(sharpenFilterAction,
                "src/main/resources/changeImageIcons/sharpenFilter.png",
                "Sharpen filter"));

        tb.add(getConfiguredButtonToToolbar(embossAction,
                "src/main/resources/changeImageIcons/emboss.png",
                "Emboss"));

        tb.add(getConfiguredButtonToToolbar(waterColorAction,
                "src/main/resources/changeImageIcons/waterColor.png",
                "Water color"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(turnImageAction,
                "src/main/resources/changeImageIcons/turnImage.png",
                "Turn image"));


        tb.addSeparator();


        tb.add(getConfiguredButtonToToolbar(gammaCorrectionAction,
                "src/main/resources/changeImageIcons/gammaCorrection.png",
                "Gamma correction"));

        return  tb;
    }


    private JToolBar constructVisualizationOfVolumeDensityToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        tb.add(getConfiguredButtonToToolbar(openConfigurationAction,
                "src/main/resources/utilIcons/openConfig.png",
                "Open configuration"));

        tb.add(getConfiguredToggleButtonToToolbar(enableAbsorptionAction,
                "src/main/resources/utilIcons/enableAbsorption.png",
                "Enable absorption",
                enableAbsorptionAction::addToggleButton
        ));

        tb.add(getConfiguredToggleButtonToToolbar(enableEmissionAction,
                "src/main/resources/utilIcons/enableEmission.png",
                "Enable emissionn",
                enableEmissionAction::addToggleButton
        ));

        tb.add(getConfiguredButtonToToolbar(runRenderAction,
                "src/main/resources/utilIcons/runRender.png",
                "Run rendering"));

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


    private JButton getConfiguredButtonToToolbar(ActionListener action, String iconPath, String toolTipText) {
        JButton button = new JButton();
        button.addActionListener(action);
        button.setIcon(new ImageIcon(iconPath));
        button.setToolTipText(toolTipText);
        return button;
    }

    private JToggleButton getConfiguredToggleButtonToToolbar(ActionListener action, String iconPath, String toolTipText, Consumer<JToggleButton> methodToInvokeAfterConfig) {
        JToggleButton button = new JToggleButton();
        button.addActionListener(action);
        button.setIcon(new ImageIcon(iconPath));
        button.setToolTipText(toolTipText);
        methodToInvokeAfterConfig.accept(button);
        return button;
    }

    private JMenuItem getConfiguredMenuItem(ActionListener action, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }

    private JRadioButtonMenuItem getConfiguredRadioMenuItem(ActionListener action, String name, Consumer<JRadioButtonMenuItem> methodToInvokeAfterConfig) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
        item.addActionListener(action);
        methodToInvokeAfterConfig.accept(item);
        return item;
    }
}
