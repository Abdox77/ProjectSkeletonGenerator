package View.toolPanel.toolButtons;

import Controllers.toolPanelObserver;
import View.tinkerPanel.tools.cardinality;

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

//            String childCard = JOptionPane.showInputDialog("Please enter the child side cardinality : {0..n, 1..1, ...}");
//            if (childCard == null || childCard.isBlank()) return;
//
//            String parentCard = JOptionPane.showInputDialog("Please enter the parent side cardinality : {0..n, 1..1, ...}");
//            if (parentCard == null || parentCard.isBlank()) return;

//            cardinality cardinality = new cardinality(parent, child);
            observer.onInheritanceCreate(parent, child);
        });
    }
}
