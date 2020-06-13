package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;


@SingletonBean
public class OrderedDitheringAction extends AbstractAction {
    @Inject
    SecondComponent secondComponent;


    @Inject
    ThirdComponent thirdComponent;


    int[][] matrix = {
            {0, 8, 2, 10},
            {12, 4, 14, 6},
            {3, 11, 1, 9},
            {15, 7 ,13, 5}
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        thirdComponent.setImage(performOrderedDither(secondComponent.getImage(), matrix));
    }


    public static BufferedImage performOrderedDither(BufferedImage image, int[][] matrix) {
        BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());


        double factor = (double)matrix.length * matrix.length / 256;

        for (int y = 0; y < image.getHeight(); y ++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color initial = new Color(image.getRGB(x, y));

                int compValue = matrix[y % matrix.length][x % matrix.length];

                Color resColor = new Color(
                        (initial.getRed() * factor > compValue) ? 255 : 0,
                        (initial.getGreen() * factor > compValue) ? 255 : 0,
                        (initial.getBlue() * factor > compValue) ? 255 : 0
                );

                result.setRGB(x, y, resColor.getRGB());
            }
        }

        return result;
    }
}
