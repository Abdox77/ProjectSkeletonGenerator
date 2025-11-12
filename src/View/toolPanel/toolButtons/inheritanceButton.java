package View.toolPanel.toolButtons;

import Controllers.toolPanelObserver;

import javax.swing.*;

public class inheritanceButton extends AtoolButton {
    private static final String ICON_NAME = "inheritance.png";
    private static final String MESSAGE_INFO = "Create an inheritance relation";
    public inheritanceButton(toolPanelObserver observer) {
        super(ICON_NAME, MESSAGE_INFO);
        addActionListener(e -> {
            String parent = JOptionPane.showInputDialog("Please enter the parent class");
            if (parent == null || parent.isBlank()) return;
            String child = JOptionPane.showInputDialog("Please enter the child class");
            if (child == null || child.isBlank()) return;
            observer.onInheritanceCreate(parent, child);
        });
    }
}
