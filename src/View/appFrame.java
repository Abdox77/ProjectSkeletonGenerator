package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import View.tinkerPanel.tinkerPanel;
import View.toolPanel.toolPanel;

public class appFrame extends JFrame {
    private final toolPanel toolPanel;
    private final tinkerPanel tinkerPanel;
    public static final int WIDTH = 1080, HEIGHT = 1000;
    public static final String title = "Project Skeleton Generator";
    private Point dragOffset;
    private Rectangle normalBounds;
    private boolean isMaximized = false;

    public appFrame() {
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        
        add(createCustomTitleBar(), BorderLayout.NORTH);
        
        tinkerPanel = new tinkerPanel();
        tinkerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 3, Color.BLACK));

        toolPanel = new toolPanel(tinkerPanel);
        toolPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 3, 3, Color.BLACK));
        
        add(tinkerPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(75, 75, 75));
        titleBar.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
        titleBar.setPreferredSize(new Dimension(WIDTH, 40));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        titleBar.add(titleLabel, BorderLayout.WEST);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonsPanel.setBackground(new Color(75, 75, 75));
        
        buttonsPanel.add(createTitleBarButton("-", new Color(70, 70, 70), e -> setState(ICONIFIED)));
        buttonsPanel.add(createTitleBarButton("□", new Color(70, 70, 70), e -> toggleMaximize()));
        buttonsPanel.add(createTitleBarButton("x", new Color(200, 50, 50), e -> System.exit(0)));
        
        titleBar.add(buttonsPanel, BorderLayout.EAST);
        makeDraggable(titleBar);
        return titleBar;
    }
    
    private void toggleMaximize() {
        if (isMaximized) {
            setBounds(normalBounds);
            isMaximized = false;
        } else {
            normalBounds = getBounds();
            setExtendedState(MAXIMIZED_BOTH);
            setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds());
            isMaximized = true;
        }
    }
    
    private JButton createTitleBarButton(String text, Color bg, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.addActionListener(action);
        return btn;
    }
    
    private void makeDraggable(JPanel titleBar) {
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { dragOffset = e.getPoint(); }
            public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) toggleMaximize(); }
        });
        
        titleBar.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isMaximized) {
                    isMaximized = false;
                    setSize(WIDTH, HEIGHT);
                    dragOffset = new Point(getWidth() / 2, dragOffset.y);
                }
                Point p = e.getLocationOnScreen();
                setLocation(p.x - dragOffset.x, p.y - dragOffset.y);
            }
        });
    }

    public void render() { setVisible(true); }
}
