package View.tinkerPanel.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class associationForte extends JComponent {
    private final classBox from;
    private final classBox to;
    private final String cardFrom;
    private final String cardTo;
    private final int ARROW_SIZE = 10;
    private int relationIndex = 0;
    private static final int LINE_OFFSET = 15;

    public associationForte(classBox from, classBox to, String cardFrom, String cardTo) {
        this.from = from;
        this.to = to;
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        setOpaque(false);
    }

    public void setRelationIndex(int index) {
        this.relationIndex = index;
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
        Point2D p1 = applyOffset(points[0], fromCenter, toCenter);
        Point2D p2 = applyOffset(points[1], fromCenter, toCenter);

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());

        g2.draw(new Line2D.Double(p1, p2));
        drawFilledArrow(g2, p2, angle);

        drawCardinality(g2, p1, angle, cardFrom, true);
        drawCardinality(g2, p2, angle, cardTo, false);

        g2.dispose();
    }

    private Point2D applyOffset(Point2D point, Point2D fromCenter, Point2D toCenter) {
        if (relationIndex == 0) return point;

        double angle = Math.atan2(toCenter.getY() - fromCenter.getY(), toCenter.getX() - fromCenter.getX());
        double perpAngle = angle + Math.PI / 2;
        double offset = relationIndex * LINE_OFFSET;

        return new Point2D.Double(
                point.getX() + offset * Math.cos(perpAngle),
                point.getY() + offset * Math.sin(perpAngle)
        );
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

    private void drawFilledArrow(Graphics2D g, Point2D tip, double angle) {
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = (int) tip.getX();
        yPoints[0] = (int) tip.getY();

        double backAngle1 = angle + Math.PI - Math.PI / 6;
        double backAngle2 = angle + Math.PI + Math.PI / 6;

        xPoints[1] = (int) (tip.getX() + ARROW_SIZE * Math.cos(backAngle1));
        yPoints[1] = (int) (tip.getY() + ARROW_SIZE * Math.sin(backAngle1));

        xPoints[2] = (int) (tip.getX() + ARROW_SIZE * Math.cos(backAngle2));
        yPoints[2] = (int) (tip.getY() + ARROW_SIZE * Math.sin(backAngle2));

        g.setColor(Color.BLACK);
        g.fillPolygon(xPoints, yPoints, 3);
        g.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawCardinality(Graphics2D g, Point2D point, double angle, String card, boolean isFrom) {
        int offset = 15;
        double perpAngle = angle + Math.PI / 2;
        int x = (int) (point.getX() + offset * Math.cos(perpAngle));
        int y = (int) (point.getY() + offset * Math.sin(perpAngle));

        if (isFrom) {
            x += (int) (ARROW_SIZE * Math.cos(angle));
            y += (int) (ARROW_SIZE * Math.sin(angle));
        }
        g.drawString(card, x, y);
    }

    public classBox getFrom() { return from; }
    public classBox getTo() { return to; }
    public String getCardFrom() { return cardFrom; }
    public String getCardTo() { return cardTo; }
}
