package View.toolPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Controllers.toolPanelObserver;
import View.appFrame;
import View.toolPanel.toolButtons.AtoolButton;
import View.toolPanel.toolButtons.aggregationButton;
import View.toolPanel.toolButtons.classBoxButton;
import View.toolPanel.toolButtons.compostionButton;
import View.toolPanel.toolButtons.genCodeButton;
import View.toolPanel.toolButtons.inheritanceButton;

public class toolPanel extends JPanel {
    private toolPanelObserver observer;
    private final static Color TOOLPANEL_BACKGROUND = Color.gray;
    private List<AtoolButton> toolButtons = new ArrayList<>();
    private genCodeButton genCodeButton;

//    public toolPanel(int Width, int Height) {
//        this.setPreferredSize(new Dimension(Width,Height));
//        this.setLayout(new FlowLayout());
//    }

    public toolPanel(toolPanelObserver observer) {
        Dimension dimension = new Dimension(appFrame.WIDTH / 4, appFrame.HEIGHT);
        setPreferredSize(dimension);
        setLayout(new FlowLayout());
        setBackground(TOOLPANEL_BACKGROUND);
        this.observer = observer;
        toolButtons.add(new classBoxButton(observer));
        toolButtons.add(new inheritanceButton(observer));
        toolButtons.add(new aggregationButton(observer));
        toolButtons.add(new compostionButton(observer));
        for(AtoolButton button : toolButtons){
            add(button);
        }
        genCodeButton = new genCodeButton();

        JPanel genCodePanel = new JPanel();
        genCodePanel.setPreferredSize(this.getPreferredSize());
        genCodePanel.setLayout(null);
        addGenerateCodeButton(genCodePanel.getPreferredSize().height, genCodePanel.getPreferredSize().width);
        genCodePanel.setBackground(TOOLPANEL_BACKGROUND);
        genCodePanel.add(genCodeButton);
        genCodePanel.setOpaque(true);
        genCodePanel.setVisible(true);
        add(genCodePanel);
        revalidate();
        // System.out.println("The height of the genCodePanel is : " +  genCodePanel.getPreferredSize().height);
        // System.out.println("The width of the genCodePanel is : " +  genCodePanel.getPreferredSize().width);
        // System.out.println("The actual height of the genCodePanel is : " +  genCodePanel.getSize().height);
        // System.out.println("The actual width of the genCodePanel is : " +  genCodePanel.getSize().width);
    }

    private void addGenerateCodeButton(int height, int width)
    {
        genCodeButton.setBounds(this.getBounds());
        genCodeButton.setBackground(Color.BLACK);
        genCodeButton.setForeground(Color.black);
        genCodeButton.setOpaque(true);
        genCodeButton.setSize(new Dimension(150, 40));
        genCodeButton.setLocation(width / 2 - 80, height / 2);
        genCodeButton.setFocusable(false);
//        add(genCodeButton);
        // System.out.println("The width of the tinkerPanel : " + this.getPreferredSize().width);

        genCodeButton.addActionListener(e -> {
            observer.generateCode();
        });
    }

    public void setToolPanelObserver(toolPanelObserver toolPanelObserver) {
        observer = toolPanelObserver;
    }
}
