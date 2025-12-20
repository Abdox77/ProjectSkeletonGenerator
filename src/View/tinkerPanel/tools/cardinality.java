package View.tinkerPanel.tools;

import java.awt.Graphics;

import javax.swing.text.JTextComponent;

public class cardinality extends JTextComponent {
    private final int x;
    private final int y;
    private final String s;

    public cardinality(String _s, int _x, int _y) {
        super();
        x = _x;
        y = _y;
        s = _s;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(s, x, y);
    }
}
