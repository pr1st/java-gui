package program.gui.dialogs;

import program.logic.configs.BSplineConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Consumer;

public class PickObjectDialog extends JDialog {
    private class IndexAndColor {
        int id;
        Color color;

        IndexAndColor(int id, Color color) {
            this.id = id;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Id: " + id + "  Color:(" + color.getRed() + " " + color.getGreen() +  " " + color.getBlue() + ")";
        }
    }


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<IndexAndColor> componentList;

    private Consumer<Integer> callback;
    public PickObjectDialog(Consumer<Integer> callback, List<BSplineConfig> bsplines, int selectedId) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);

        setTitle("Pick Object");


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        IndexAndColor[] toList = new IndexAndColor[bsplines.size()];
        for(int i = 0; i < bsplines.size(); i++) {
            toList[i] = new IndexAndColor(i, bsplines.get(i).color);
        }

        componentList.setListData(toList);
        if (selectedId != -1)
            componentList.setSelectedIndex(selectedId);

        pack();
        setVisible(true);
    }

    private void onOK() {
        if (componentList.getSelectedValue() != null) {
            callback.accept(componentList.getSelectedValue().id);
        } else {
            callback.accept(null);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
