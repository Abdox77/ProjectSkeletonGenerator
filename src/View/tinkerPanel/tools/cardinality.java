package View.tinkerPanel.tools;

import View.tinkerPanel.elements.classBox;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class cardinality extends JTextComponent {
    private final int x;
    private final int y;
    private final String s;

    public cardinality(String _s, int _x, int _y) {
        super();
        x = _x;
        y = _y;
        s = _s;
//        setBounds(_x, _y, 200, 200)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(s, x, y);
    }
}
