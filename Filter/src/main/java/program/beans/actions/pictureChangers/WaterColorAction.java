package program.beans.actions.pictureChangers;

import program.beans.components.SecondComponent;
import program.beans.components.ThirdComponent;
import program.iocContainer.Inject;
import program.iocContainer.SingletonBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


@SingletonBean
public class WaterColorAction extends AbstractAction {

    @Inject
    SecondComponent secondComponent;

    @Inject
    ThirdComponent thirdComponent;


    int[][] filterMatrix = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage image = medianFilter(secondComponent.getImage(), 5);
        thirdComponent.setImage(FilterUtil.applyFilter(image, filterMatrix, 1, null));
    }

    public static BufferedImage medianFilter(BufferedImage image, int scale) {
        if (!(scale == 3 || scale == 5))
            throw new IllegalArgumentException();

        BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                List<Integer> redList = new ArrayList<>();
                List<Integer> greenList = new ArrayList<>();
                List<Integer> blueList = new ArrayList<>();

                int center = (scale / 2);

                for (int i = -center; i < center + 1; i++) {
                    for (int j = -center; j < center + 1; j++) {
                        Color initColor;
                        initColor = FilterUtil.getColorOrDefaultBlack(image, x +j, y + i);


                        redList.add(initColor.getRed());
                        greenList.add(initColor.getGreen());
                        blueList.add(initColor.getBlue());
                    }
                }

                redList.sort(null);
                greenList.sort(null);
                blueList.sort(null);

                result.setRGB(x, y, new Color(
                        redList.get(redList.size() / 2 + 1),
                        greenList.get(greenList.size() / 2 + 1),
                        blueList.get(blueList.size() / 2 + 1)
                ).getRGB());

                redList.clear();
                greenList.clear();
                blueList.clear();
            }
        }

        return result;
    }

}
