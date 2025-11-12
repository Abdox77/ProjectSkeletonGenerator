package View;
import View.tinkerPanel.tinkerPanel;
import View.toolPanel.toolPanel;

import javax.swing.*;
import java.awt.*;


public class appFrame extends JFrame {
    private final toolPanel toolPanel;
    private final tinkerPanel tinkerPanel;
    public static final int HEIGHT = 1080;
    public static final int WIDTH = 1080;
    public static final String title = "Project Skeleton Generator";


    public appFrame() {
        setTitle(title);
        setSize(WIDTH, HEIGHT);
        tinkerPanel = new tinkerPanel();
        toolPanel = new toolPanel(tinkerPanel);
        setLayout(new BorderLayout(10, 5));
        add(tinkerPanel, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

//    public void appListeners() {
        // this.addWindowFocusListener();
        // i should add it later, when the window is maximized so that i can
        // change the size of the toolPanel and the size of the tinkeringPanel
//    }

    public void render() {
        this.setVisible(true);
    }
}
