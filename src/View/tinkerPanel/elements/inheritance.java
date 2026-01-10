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

public class inheritance extends JComponent {
    private final classBox child;
    private final classBox parent;
    private final int ARROW_SIZE = 12;
    private int relationIndex = 0;
    private static final int LINE_OFFSET = 15;

    public inheritance(classBox child, classBox parent) {
        this.child = child;
        this.parent = parent;
        setOpaque(false);
    }

    public void setRelationIndex(int index) {
        this.relationIndex = index;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        java.awt.Point childLoc = child.getLocation();
        java.awt.Point parentLoc = parent.getLocation();
        
        double childCenterX = childLoc.x + child.getWidth() / 2.0;
        double childCenterY = childLoc.y + child.getHeight() / 2.0;
        double parentCenterX = parentLoc.x + parent.getWidth() / 2.0;
        double parentCenterY = parentLoc.y + parent.getHeight() / 2.0;

        Point2D childCenter = new Point2D.Double(childCenterX, childCenterY);
        Point2D parentCenter = new Point2D.Double(parentCenterX, parentCenterY);

        Point2D[] points = getConnectionPoints(childCenter, parentCenter);
        Point2D p1 = applyOffset(points[0], childCenter, parentCenter);
        Point2D p2 = applyOffset(points[1], childCenter, parentCenter);

        double angle = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        
        Point2D lineEnd = new Point2D.Double(
            p2.getX() - ARROW_SIZE * Math.cos(angle),
            p2.getY() - ARROW_SIZE * Math.sin(angle)
        );
        
        g2.draw(new Line2D.Double(p1, lineEnd));
        
        drawInheritanceHead(g2, p2, angle);
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

    private void drawInheritanceHead(Graphics2D g, Point2D tip, double angle) {
        double arrowHalfWidth = ARROW_SIZE * 0.5;
        
        int tipX = (int) tip.getX();
        int tipY = (int) tip.getY();
        
        double baseCenterX = tip.getX() - ARROW_SIZE * Math.cos(angle);
        double baseCenterY = tip.getY() - ARROW_SIZE * Math.sin(angle);
        
        double perpAngle = angle + Math.PI / 2;
        
        int leftX = (int) (baseCenterX + arrowHalfWidth * Math.cos(perpAngle));
        int leftY = (int) (baseCenterY + arrowHalfWidth * Math.sin(perpAngle));
        
        int rightX = (int) (baseCenterX - arrowHalfWidth * Math.cos(perpAngle));
        int rightY = (int) (baseCenterY - arrowHalfWidth * Math.sin(perpAngle));

        int[] xPoints = {tipX, leftX, rightX};
        int[] yPoints = {tipY, leftY, rightY};
        
        Polygon arrow = new Polygon(xPoints, yPoints, 3);
        
        g.setColor(Color.WHITE);
        g.fillPolygon(arrow);
        g.setColor(Color.BLACK);
        g.drawPolygon(arrow);
    }

    public classBox getChild() { return child; }
    public classBox getParent() { return parent; }
}
