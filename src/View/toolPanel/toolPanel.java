package View.toolPanel;

import View.appFrame;
import View.toolPanel.toolButtons.*;
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
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(TOOLPANEL_BACKGROUND);
        observer = observer;
        toolButtons.add(new classBoxButton(observer));
        toolButtons.add(new inheritanceButton(observer));
        toolButtons.add(new aggregationButton(observer));
        toolButtons.add(new compostionButton(observer));

        for(AtoolButton button : toolButtons){
            add(button);
        }
    }

    public void setToolPanelObserver(toolPanelObserver toolPanelObserver) {
        observer = toolPanelObserver;
    }
}
