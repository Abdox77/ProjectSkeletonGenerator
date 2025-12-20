package View.tinkerPanel.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class inheritance extends JComponent {
    Line2D line;
    Point2D p1;
    Point2D p2;
    private final classBox child;
    private final classBox parent;
    private final int ALPHA = 20;

    public inheritance(classBox child, classBox parent) {
        System.out.println(child.getName() + " " + parent.getName());
        this.child = child;
        this.parent = parent;
        p1 = child.getLocation();
        p2 = parent.getLocation();
        line = new Line2D.Double(p1, p2);
        setOpaque(false);
    }

//    private void setLayout() {
//        int x = Math.min(child.getX(), parent.getX());
//        int y = Math.min(child.getY(), parent.getY());
//        int w = Math.abs(child.getX() - parent.getX());
//        int h = Math.abs(child.getY() - parent.getY());
//        super.setBounds(x, y, w, h);
//    }

    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2 = (Graphics2D) g.create();
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       g2.setStroke(new BasicStroke(2f));
       g2.setColor(Color.black);

       System.out.println("inheritance paintComponent " + child.getX() + " " + child.getHeight());
       System.out.println("inheritance paintComponent " + parent.getWidth() + " " + parent.getHeight());
       Point2D p1 = SwingUtilities.convertPoint(child, child.getWidth() + 20, child.getHeight() / 2, this);
       Point2D p2 = SwingUtilities.convertPoint(parent, 0, parent.getHeight() / 2, this);
       g2.draw(new Line2D.Double(p1, p2));

       drawInheritanceHead(g2, (int)p1.getX(), (int)p1.getY());
       g2.dispose();
    }

    private void drawInheritanceHead(Graphics2D g, int a, int b) {
        int alpha = 20;
        int x = a - alpha;
        int y = b;

        int[] xPoly = {x , x + alpha, x + alpha};
        int[] yPoly = {y, y + alpha, y - alpha};
        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        g.drawPolygon(poly);
    }
}
