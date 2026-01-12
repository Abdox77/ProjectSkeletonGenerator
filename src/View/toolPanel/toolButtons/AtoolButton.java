package View.toolPanel.toolButtons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class AtoolButton extends JButton {
    private String messageInfo = null;
    private static final String IMG_DIR = "images/";
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
