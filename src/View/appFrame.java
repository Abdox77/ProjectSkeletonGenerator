package View;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import View.tinkerPanel.tinkerPanel;
import View.toolPanel.toolPanel;


public class appFrame extends JFrame {
    private final toolPanel toolPanel;
    private final tinkerPanel tinkerPanel;
    public static final int HEIGHT = 1080;
    public static final int WIDTH = 1080;
    public static final String title = "Project Skeleton Generator";


    public appFrame() {
        setTitle(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        tinkerPanel = new tinkerPanel();
        toolPanel = new toolPanel(tinkerPanel);
        setLayout(new BorderLayout(10, 5));
        add(tinkerPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void render() {
        this.setVisible(true);
    }
}
