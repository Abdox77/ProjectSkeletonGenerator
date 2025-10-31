package View.tinkerPanel.elements;

import javax.swing.*;
import java.awt.*;

public class classBox extends JPanel {
    private final JLabel className = new JLabel();
    private final Color CLASSBOX_COLOR = Color.LIGHT_GRAY;

    // To implement later
    // List<classAtributes> ;
    // List<methods>

    public classBox(String title) {
        this.setLayout(null);

        // i should calculate the bounds of the class and where to position it

        this.setBounds(50, 50, 200, 200);
        this.setBackground(CLASSBOX_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.className.setText(title);
        this.className.setBounds(0, 0, 200, 200);
        this.className.setHorizontalAlignment(JLabel.CENTER);
        this.setFocusable(false);
        this.add(this.className);
    }
}
