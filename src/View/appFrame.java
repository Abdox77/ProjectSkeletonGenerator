package View;
import View.tinkerPanel.tinkerPanel;
import View.toolPanel.toolPanel;

import javax.swing.*;
import java.awt.*;


public class appFrame extends JFrame {
    private toolPanel toolPanel;
    private tinkerPanel tinkerPanel;

    public static final int HEIGHT = 1080;
    public static final int WIDTH = 1080;
    public static final String title = "Project Skeleton Generator";


    public appFrame() {
        this.setTitle(title);
        this.setSize(WIDTH, HEIGHT);
        this.tinkerPanel = new tinkerPanel();
        this.toolPanel = new toolPanel(this.tinkerPanel);
        this.setLayout(new BorderLayout(10, 5));
        this.add(this.tinkerPanel, BorderLayout.CENTER);
        this.add(this.toolPanel, BorderLayout.LINE_START);
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
