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

public class associationFaible extends JComponent {
    private final classBox from;
    private final classBox to;
    private final String cardFrom;
    private final String cardTo;
    private final int ARROW_SIZE = 10;
    private int relationIndex = 0;
    private static final int LINE_OFFSET = 15;

    public associationFaible(classBox from, classBox to, String cardFrom, String cardTo) {
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

        float[] dashPattern = {10, 5};
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2.setColor(Color.BLACK);

        if (from == to) {
            drawSelfRelation(g2);
            g2.dispose();
            return;
        }

        Point2D fromCenter = SwingUtilities.convertPoint(from, from.getWidth() / 2, from.getHeight() / 2, this);
        Point2D toCenter = SwingUtilities.convertPoint(to, to.getWidth() / 2, to.getHeight() / 2, this);

        Point2D[] points = getConnectionPoints(fromCenter, toCenter);
        Point2D p1 = applyOffset(points[0], fromCenter, toCenter);
        Point2D p2 = applyOffset(points[1], fromCenter, toCenter);

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());

        g2.draw(new Line2D.Double(p1, p2));
        g2.setStroke(new BasicStroke(2f));
        drawOpenArrow(g2, p2, angle);

        drawCardinality(g2, p1, angle, cardFrom, true);
        drawCardinality(g2, p2, angle, cardTo, false);

        g2.dispose();
    }

    private void drawSelfRelation(Graphics2D g2) {
        Point2D loc = SwingUtilities.convertPoint(from.getParent(), from.getX(), from.getY(), this);
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int w = from.getWidth();
        int h = from.getHeight();

        int loopSize = 40 + relationIndex * LINE_OFFSET;
        int startX = x + w;
        int startY = y + h / 3;
        int endX = x + w / 2 + 20;
        int endY = y;

        g2.draw(new Line2D.Double(startX, startY, startX + loopSize, startY));
        g2.draw(new Line2D.Double(startX + loopSize, startY, startX + loopSize, endY - loopSize));
        g2.draw(new Line2D.Double(startX + loopSize, endY - loopSize, endX, endY - loopSize));
        g2.draw(new Line2D.Double(endX, endY - loopSize, endX, endY));

        g2.setStroke(new BasicStroke(2f));
        drawOpenArrow(g2, new Point2D.Double(endX, endY), -Math.PI / 2);

        g2.drawString(cardFrom, startX + 5, startY - 5);
        g2.drawString(cardTo, endX + 5, endY - loopSize + 15);
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

    private void drawOpenArrow(Graphics2D g, Point2D tip, double angle) {
        double backAngle1 = angle + Math.PI - Math.PI / 6;
        double backAngle2 = angle + Math.PI + Math.PI / 6;

        int x1 = (int) (tip.getX() + ARROW_SIZE * Math.cos(backAngle1));
        int y1 = (int) (tip.getY() + ARROW_SIZE * Math.sin(backAngle1));

        int x2 = (int) (tip.getX() + ARROW_SIZE * Math.cos(backAngle2));
        int y2 = (int) (tip.getY() + ARROW_SIZE * Math.sin(backAngle2));

        g.drawLine((int) tip.getX(), (int) tip.getY(), x1, y1);
        g.drawLine((int) tip.getX(), (int) tip.getY(), x2, y2);
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
