package View.toolPanel.toolButtons;

import java.awt.*;
import javax.swing.*;

public abstract class AtoolButton extends JButton {
    private String messageInfo = null;
    private static final String IMG_DIR = "src/images/";
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = 60;
    private static final Color BUTTON_COLOR = Color.blue;


    AtoolButton(String imageIcon, String messageInfo) {
        super("", new ImageIcon(IMG_DIR + imageIcon));
        setToolTipText(messageInfo);
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        messageInfo = messageInfo;
        setOpaque(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setBackground(BUTTON_COLOR);
    }

    public String getMessageInfo() {
        return this.messageInfo;
    }
}
