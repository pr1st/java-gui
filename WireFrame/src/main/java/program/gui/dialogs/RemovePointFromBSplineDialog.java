package program.gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RemovePointFromBSplineDialog extends JDialog {

    private class IndexAndPoint {
        private DecimalFormat formatter = new DecimalFormat("#.##");

        int id;
        Point2D point;

        IndexAndPoint(int id, Point2D point) {
            this.id = id;
            this.point = point;
        }

        @Override
        public String toString() {
            return "Id: " + id + "  (" + formatter.format(point.getX()) + ", " + formatter.format(point.getY()) + ")";
        }
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<IndexAndPoint> componentList;
    private JLabel overAllPoints;


    private Consumer<Point2D> callback;
    RemovePointFromBSplineDialog(Consumer<Point2D> callback, List<Point2D> givenPointsList) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        overAllPoints.setText("Overall points: " + givenPointsList.size());


        setTitle("Remove point");
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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        IndexAndPoint[] toList = new IndexAndPoint[givenPointsList.size()];
        for(int i = 0; i < givenPointsList.size(); i++) {
            toList[i] = new IndexAndPoint(i, givenPointsList.get(i));
        }

        componentList.setListData(toList);

        pack();
        setVisible(true);
    }

    private void onOK() {
        if (componentList.getSelectedValue() != null) {
            callback.accept(componentList.getSelectedValue().point);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
