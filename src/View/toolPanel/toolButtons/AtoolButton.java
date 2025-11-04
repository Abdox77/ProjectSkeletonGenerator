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
        ImageIcon tmp = new ImageIcon(IMG_DIR + imageIcon);
        Image image = tmp.getImage();
        ImageIcon icon = new ImageIcon(image.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        super.setText("");
        super.setIcon(icon);
        setToolTipText(messageInfo);
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        this.messageInfo = messageInfo;
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
