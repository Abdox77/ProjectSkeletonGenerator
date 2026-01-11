package View.toolPanel.toolButtons;

import java.awt.Frame;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Controllers.toolPanelObserver;
import View.toolPanel.Dialogs.inheritanceDialog;

public class inheritanceButton extends AtoolButton {
    private static final String ICON_NAME = "inheritance.png";
    private static final String MESSAGE_INFO = "Create an inheritance relation";

    public inheritanceButton(toolPanelObserver observer) {
        super(ICON_NAME, MESSAGE_INFO);
        addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            List<String> classNames = observer.getClassNames();
            
            if (classNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please create some classes first", 
                    "No Classes", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Set<String> classesWithParent = observer.getClassesWithParent();
            inheritanceDialog dialog = new inheritanceDialog(parentFrame, classNames, classesWithParent);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                String parent = dialog.getParentClass();
                String child = dialog.getChildClass();
                observer.onInheritanceCreate(parent, child);
            }
        });
    }
}
