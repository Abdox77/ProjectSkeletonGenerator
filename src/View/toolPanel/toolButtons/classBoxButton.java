package View.toolPanel.toolButtons;

import Controllers.toolPanelObserver;
import javax.swing.*;

public class classBoxButton extends AtoolButton {
    private toolPanelObserver observer;
    static final private String MESSAGE_INFO = "Create a class";
    private static final String  ICON_NAME = "class.png";

    public classBoxButton(toolPanelObserver observer) {
        super(ICON_NAME, MESSAGE_INFO);
        this.observer = observer;

        addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Class Name:");
            if (name != null && !name.isBlank()) {
                observer.onClassCreate(name.trim());
                System.out.println("Class Name: " + name);
            }
        });
    }
}
