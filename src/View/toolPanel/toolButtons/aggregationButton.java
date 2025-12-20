package View.toolPanel.toolButtons;

import java.awt.Frame;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Controllers.toolPanelObserver;
import View.toolPanel.Dialogs.aggregationDialog;

public class aggregationButton extends AtoolButton {
    private toolPanelObserver observer;
    static final String MESSAGE_INFO = "Create a aggregation";
    private static final String  ICON_NAME = "aggregation.png";

    public aggregationButton(toolPanelObserver _observer) {
        super(ICON_NAME, MESSAGE_INFO);
        observer = _observer;
        addActionListener(e-> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            List<String> classNames = observer.getClassNames();
            
            if (classNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please create some classes first", 
                    "No Classes", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            aggregationDialog dialog = new aggregationDialog(parentFrame, classNames);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                String firstClass = dialog.getFirstClass();
                String secondClass = dialog.getSecondClass();
                String cardFirstClass = dialog.getFirstCardinality();
                String cardSecondClass = dialog.getSecondCardinality();
                
                observer.onAggregationCreate(firstClass, secondClass, cardFirstClass, cardSecondClass);
            }
        });
    }
}
