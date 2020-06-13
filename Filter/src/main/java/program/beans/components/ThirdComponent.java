package program.beans.components;

import program.iocContainer.SingletonBean;
import java.awt.image.BufferedImage;

@SingletonBean
public class ThirdComponent extends AbstractJComponent {

    public boolean isImageSet = false;

    @Override
    public void restoreImage() {
        super.restoreImage();
        isImageSet = false;
    }

    @Override
    public void setImage(BufferedImage image) {
        super.setImage(image);
        isImageSet = true;
    }
}
