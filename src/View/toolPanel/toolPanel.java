package View.toolPanel;

import View.appFrame;
import View.toolPanel.toolButtons.AtoolButton;
import View.toolPanel.toolButtons.classBoxButton;
import Controllers.toolPanelObserver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class toolPanel extends JPanel {
    private toolPanelObserver observer;
    private final static Color TOOLPANEL_BACKGROUND = Color.gray;
    private List<AtoolButton> toolButtons = new ArrayList<>();

//    public toolPanel(int Width, int Height) {
//        this.setPreferredSize(new Dimension(Width,Height));
//        this.setLayout(new FlowLayout());
//    }

    public toolPanel(toolPanelObserver observer) {
        Dimension dimension = new Dimension(appFrame.WIDTH / 5, appFrame.HEIGHT);
        this.setPreferredSize(dimension);
        this.setLayout(new FlowLayout());
        this.setBackground(this.TOOLPANEL_BACKGROUND);
        this.observer = observer;
        this.toolButtons.add(new classBoxButton(this.observer));
        for(AtoolButton button : toolButtons){
            this.add(button);
        }
    }

    public void setToolPanelObserver(toolPanelObserver toolPanelObserver) {
        this.observer = toolPanelObserver;
    }
}
