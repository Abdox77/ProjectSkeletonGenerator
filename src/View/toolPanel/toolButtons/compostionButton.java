package View.toolPanel.toolButtons;

import Controllers.toolPanelObserver;
import javax.swing.*;

public class compostionButton extends AtoolButton {
    private toolPanelObserver observer;
    private static final String ICON_NAME = "composition.png";
    private static final String MESSAGE_INFO = "Create a composition";

    public compostionButton(toolPanelObserver _observer) {
        super(ICON_NAME, MESSAGE_INFO);
        observer = _observer;
        addActionListener(e-> {
            String firstClass = JOptionPane.showInputDialog("first <>--> second; Please first class name : ");
            if (firstClass != null && !firstClass.isBlank())
                return;
            String secondClass = JOptionPane.showInputDialog("fist <>--> second; Please second class name : ");
            if (secondClass != null && !secondClass.isBlank())
                return;
            String cardFirstClass = JOptionPane.showInputDialog("first <>--> second; Please enter first class cardinality");
            if (cardFirstClass != null && !cardFirstClass.isBlank())
                return;
            String cardSecondClass = JOptionPane.showInputDialog("first <>--> second; Please enter second class cardinality");
            if  (cardSecondClass != null && !cardSecondClass.isBlank())
                return;

            observer.onCompositionCreate(firstClass, secondClass, cardFirstClass, cardSecondClass);
        });
    }
}
