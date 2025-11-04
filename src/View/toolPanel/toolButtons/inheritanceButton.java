package View.toolPanel.toolButtons;

import Controllers.toolPanelObserver;

public class inheritanceButton extends AtoolButton{
    private toolPanelObserver observer;
    private static final String ICON_NAME = "inheritance.png";
    private static final String MESSAGE_INFO = "Create an inheritance relation";
    public inheritanceButton() {
        super(ICON_NAME, MESSAGE_INFO);
        addActionListener(e -> {

        });
    }
}
