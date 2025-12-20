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
    private final classBox child;
    private final classBox parent;
    private final int ALPHA = 15;

    public inheritance(classBox child, classBox parent) {
        this.child = child;
        this.parent = parent;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        Point2D childCenter = SwingUtilities.convertPoint(child, child.getWidth() / 2, child.getHeight() / 2, this);
        Point2D parentCenter = SwingUtilities.convertPoint(parent, parent.getWidth() / 2, parent.getHeight() / 2, this);

        Point2D[] points = getConnectionPoints(childCenter, parentCenter);
        Point2D p1 = points[0]; 
        Point2D p2 = points[1]; 

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        Point2D lineEnd = new Point2D.Double(
            p2.getX() - ALPHA * Math.cos(angle),
            p2.getY() - ALPHA * Math.sin(angle)
        );
        
        g2.draw(new Line2D.Double(p1, lineEnd));
        drawInheritanceHead(g2, p2, p1);
        g2.dispose();
    }

    private Point2D[] getConnectionPoints(Point2D childCenter, Point2D parentCenter) {
        double dx = parentCenter.getX() - childCenter.getX();
        double dy = parentCenter.getY() - childCenter.getY();

        Point2D childEdge, parentEdge;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                childEdge = new Point2D.Double(childCenter.getX() + child.getWidth() / 2, childCenter.getY());
                parentEdge = new Point2D.Double(parentCenter.getX() - parent.getWidth() / 2, parentCenter.getY());
            } else {
                childEdge = new Point2D.Double(childCenter.getX() - child.getWidth() / 2, childCenter.getY());
                parentEdge = new Point2D.Double(parentCenter.getX() + parent.getWidth() / 2, parentCenter.getY());
            }
        } else {
            if (dy > 0) {
                childEdge = new Point2D.Double(childCenter.getX(), childCenter.getY() + child.getHeight() / 2);
                parentEdge = new Point2D.Double(parentCenter.getX(), parentCenter.getY() - parent.getHeight() / 2);
            } else {
                childEdge = new Point2D.Double(childCenter.getX(), childCenter.getY() - child.getHeight() / 2);
                parentEdge = new Point2D.Double(parentCenter.getX(), parentCenter.getY() + parent.getHeight() / 2);
            }
        }
        return new Point2D[]{childEdge, parentEdge};
    }

    private void drawInheritanceHead(Graphics2D g, Point2D tip, Point2D from) {
        double dx = tip.getX() - from.getX();
        double dy = tip.getY() - from.getY();
        double angle = Math.atan2(dy, dx);

        int baseX = (int) (tip.getX() - ALPHA * Math.cos(angle));
        int baseY = (int) (tip.getY() - ALPHA * Math.sin(angle));

        int[] xPoly = {
            (int) tip.getX(),
            (int) (baseX + ALPHA * Math.cos(angle + Math.PI / 2) / 2),
            (int) (baseX + ALPHA * Math.cos(angle - Math.PI / 2) / 2)
        };
        int[] yPoly = {
            (int) tip.getY(),
            (int) (baseY + ALPHA * Math.sin(angle + Math.PI / 2) / 2),
            (int) (baseY + ALPHA * Math.sin(angle - Math.PI / 2) / 2)
        };

        Polygon poly = new Polygon(xPoly, yPoly, 3);
        g.setColor(Color.WHITE);
        g.fillPolygon(poly);
        g.setColor(Color.BLACK);
        g.drawPolygon(poly);
    }
}
