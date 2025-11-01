package View.tinkerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragHandler extends MouseAdapter {
    private Point last;
    private boolean snapToGrid;
    private boolean clampedToParent;
    private int grid = 20;
    private JComponent dragTarget;
    private JComponent dragTargetParent;

    public DragHandler snapToGrid(int grid) {
        this.snapToGrid = true;
        this.grid = grid;
        return this;
    }

    public DragHandler clampedToParent(boolean isClamped) {
        this.clampedToParent = isClamped;
        return this;
    }

    public DragHandler(JComponent component) {
        dragTarget = component;
        dragTargetParent = (JComponent) dragTarget.getParent();
        dragTarget.addMouseListener(this);
        dragTarget.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragTarget.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        last = e.getPoint();
        dragTargetParent.setComponentZOrder(dragTarget, 0);
        dragTargetParent.revalidate();
        dragTargetParent.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragTargetParent == null)
            return;

        Point mouseInParent = SwingUtilities.convertPoint(dragTarget, e.getPoint(), dragTargetParent);

        int newX = mouseInParent.x - last.x;
        int newY = mouseInParent.y - last.y;

        if (snapToGrid) {
            newX = Math.round(newX / (float)grid) * grid;
            newY = Math.round(newY / (float)grid) * grid;
        }

        if (clampedToParent) {
            int maxX = dragTargetParent.getWidth() - dragTarget.getWidth();
            int maxY = dragTargetParent.getHeight() - dragTarget.getHeight();
            newX = Math.max(0, Math.min(newX,  maxX));
            newY = Math.max(0, Math.min(newY,  maxY));
        }

        dragTarget.setLocation(newX, newY);
        dragTargetParent.revalidate();
        dragTargetParent.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragTarget.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
