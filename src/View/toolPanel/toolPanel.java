package View.toolPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Controllers.toolPanelObserver;
import View.appFrame;
import View.toolPanel.toolButtons.aggregationButton;
import View.toolPanel.toolButtons.associationFaibleButton;
import View.toolPanel.toolButtons.associationForteButton;
import View.toolPanel.toolButtons.classBoxButton;
import View.toolPanel.toolButtons.compostionButton;
import View.toolPanel.toolButtons.genCodeButton;
import View.toolPanel.toolButtons.inheritanceButton;

public class toolPanel extends JPanel {
    private final static Color TOOLPANEL_BACKGROUND = new Color(130, 130, 130);

    public toolPanel(toolPanelObserver observer) {
        Dimension dimension = new Dimension(appFrame.WIDTH / 4, appFrame.HEIGHT);
        setPreferredSize(dimension);
        setLayout(new BorderLayout());
        setBackground(TOOLPANEL_BACKGROUND);

        JPanel toolButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        toolButtonsPanel.setBackground(TOOLPANEL_BACKGROUND);
        toolButtonsPanel.add(new classBoxButton(observer));
        toolButtonsPanel.add(new inheritanceButton(observer));
        toolButtonsPanel.add(new aggregationButton(observer));
        toolButtonsPanel.add(new compostionButton(observer));
        toolButtonsPanel.add(new associationForteButton(observer));
        toolButtonsPanel.add(new associationFaibleButton(observer));
        add(toolButtonsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottomPanel.setBackground(TOOLPANEL_BACKGROUND);
        genCodeButton genCodeButton = new genCodeButton();
        genCodeButton.setPreferredSize(new Dimension(180, 45));
        genCodeButton.setBackground(new Color(45, 45, 45));
        genCodeButton.setForeground(Color.WHITE);
        genCodeButton.setFont(new Font("Arial", Font.BOLD, 14));
        genCodeButton.setFocusPainted(false);
        genCodeButton.setOpaque(true);
        genCodeButton.setContentAreaFilled(true);
        genCodeButton.setBorderPainted(true);
        genCodeButton.setEnabled(true);
        genCodeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        genCodeButton.addActionListener(e -> {
            observer.generateCode();
        });

        bottomPanel.add(genCodeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
