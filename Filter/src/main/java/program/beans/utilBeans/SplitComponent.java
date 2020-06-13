package program.beans.utilBeans;



import javax.swing.*;
import java.awt.*;

public class SplitComponent extends JComponent {


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(15,350);
    }

    @Override
    public Dimension getMaximumSize(){
        return getPreferredSize();
    }
    @Override
    public Dimension getMinimumSize(){
        return getPreferredSize();
    }
}
