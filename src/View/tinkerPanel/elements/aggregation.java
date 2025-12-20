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

public class aggregation extends JComponent {
    private final classBox from;
    private final classBox to;
    private final String cardFrom;
    private final String cardTo;
    private final int ALPHA = 12;

    public aggregation(classBox from, classBox to, String cardFrom, String cardTo) {
        this.from = from;
        this.to = to;
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        Point2D fromCenter = SwingUtilities.convertPoint(from, from.getWidth() / 2, from.getHeight() / 2, this);
        Point2D toCenter = SwingUtilities.convertPoint(to, to.getWidth() / 2, to.getHeight() / 2, this);

        Point2D[] points = getConnectionPoints(fromCenter, toCenter);
        Point2D p1 = points[0];
        Point2D p2 = points[1];

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        
        Point2D lineStart = new Point2D.Double(
            p1.getX() + 2 * ALPHA * Math.cos(angle),
            p1.getY() + 2 * ALPHA * Math.sin(angle)
        );

        g2.draw(new Line2D.Double(lineStart, p2));
        drawAggregationHead(g2, p1, angle);
        
        drawCardinality(g2, p1, angle, cardFrom, true);
        drawCardinality(g2, p2, angle, cardTo, false);
        
        g2.dispose();
    }

    private Point2D[] getConnectionPoints(Point2D fromCenter, Point2D toCenter) {
        double dx = toCenter.getX() - fromCenter.getX();
        double dy = toCenter.getY() - fromCenter.getY();

        Point2D fromEdge, toEdge;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                fromEdge = new Point2D.Double(fromCenter.getX() + from.getWidth() / 2, fromCenter.getY());
                toEdge = new Point2D.Double(toCenter.getX() - to.getWidth() / 2, toCenter.getY());
            } else {
                fromEdge = new Point2D.Double(fromCenter.getX() - from.getWidth() / 2, fromCenter.getY());
                toEdge = new Point2D.Double(toCenter.getX() + to.getWidth() / 2, toCenter.getY());
            }
        } else {
            if (dy > 0) {
                fromEdge = new Point2D.Double(fromCenter.getX(), fromCenter.getY() + from.getHeight() / 2);
                toEdge = new Point2D.Double(toCenter.getX(), toCenter.getY() - to.getHeight() / 2);
            } else {
                fromEdge = new Point2D.Double(fromCenter.getX(), fromCenter.getY() - from.getHeight() / 2);
                toEdge = new Point2D.Double(toCenter.getX(), toCenter.getY() + to.getHeight() / 2);
            }
        }
        return new Point2D[]{fromEdge, toEdge};
    }

    private void drawAggregationHead(Graphics2D g, Point2D tip, double angle) {
        int[] xPoly = new int[4];
        int[] yPoly = new int[4];

        xPoly[0] = (int) tip.getX();
        yPoly[0] = (int) tip.getY();
        xPoly[1] = (int) (tip.getX() + ALPHA * Math.cos(angle) - ALPHA * Math.sin(angle));
        yPoly[1] = (int) (tip.getY() + ALPHA * Math.sin(angle) + ALPHA * Math.cos(angle));
        xPoly[2] = (int) (tip.getX() + 2 * ALPHA * Math.cos(angle));
        yPoly[2] = (int) (tip.getY() + 2 * ALPHA * Math.sin(angle));
        xPoly[3] = (int) (tip.getX() + ALPHA * Math.cos(angle) + ALPHA * Math.sin(angle));
        yPoly[3] = (int) (tip.getY() + ALPHA * Math.sin(angle) - ALPHA * Math.cos(angle));
        Polygon poly = new Polygon(xPoly, yPoly, 4);
        g.setColor(Color.WHITE);
        g.fillPolygon(poly);
        g.setColor(Color.BLACK);
        g.drawPolygon(poly);
    }

    private void drawCardinality(Graphics2D g, Point2D point, double angle, String card, boolean isFrom) {
        int offset = 15;
        double perpAngle = angle + Math.PI / 2;
        int x = (int) (point.getX() + offset * Math.cos(perpAngle));
        int y = (int) (point.getY() + offset * Math.sin(perpAngle));
        
        if (isFrom) {
            x += (int) (2 * ALPHA * Math.cos(angle));
            y += (int) (2 * ALPHA * Math.sin(angle));
        }
        g.drawString(card, x, y);
    }
}
