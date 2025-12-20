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

public class composition extends JComponent
{
    private final classBox from;
    private final classBox to;
    private final String cardFrom;
    private final String cardTo;

    public composition(classBox _from, classBox _to, String _cardFrom, String _cardTo)
    {
        this.from = _from;
        this.to = _to;
        this.cardFrom = _cardFrom;
        this.cardTo = _cardTo;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.black);

        Point2D p1 = SwingUtilities.convertPoint(to, to.getWidth() + 40, to.getHeight() / 2, this);
        Point2D p2 = SwingUtilities.convertPoint(from, 0, from.getHeight() / 2, this);
        drawAggregationHead(g2, (int)p1.getX(), (int)p1.getY());
        g2.draw(new Line2D.Double(p1, p2));
        g2.drawString(cardFrom, (int)p1.getX() + 10, (int)p1.getY() - 15);
        g2.drawString(cardTo, (int)p2.getX() - 25, (int)p2.getY() - 15);
        g2.dispose();
    }

    private void drawAggregationHead(Graphics2D g, int a, int b)
    {
        int alpha = 20;
        int x = a - 40;
        int y = b;

        // the order: Left -> Bottom -> Right -> Top
        int[] xPoly = {x, x + alpha, x + 2 * alpha, x + alpha};
        int[] yPoly = {y, y + alpha,             y, y - alpha};

        Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
        g.setColor(Color.BLACK);
        g.fillPolygon(poly);
        g.drawPolygon(poly);
    }
}
