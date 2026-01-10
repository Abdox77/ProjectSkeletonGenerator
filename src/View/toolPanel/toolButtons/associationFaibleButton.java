package View.toolPanel.toolButtons;

import java.awt.Frame;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Controllers.toolPanelObserver;
import View.toolPanel.Dialogs.AssociationDialog;

public class associationFaibleButton extends AtoolButton {
    private toolPanelObserver observer;
    private static final String ICON_NAME = "association_faible.png";
    private static final String MESSAGE_INFO = "Create a weak association";

    public associationFaibleButton(toolPanelObserver _observer) {
        super(ICON_NAME, MESSAGE_INFO);
        observer = _observer;
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

            AssociationDialog dialog = new AssociationDialog(parentFrame, classNames, "Create Weak Association");
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String firstClass = dialog.getFirstClass();
                String secondClass = dialog.getSecondClass();
                String cardFirstClass = dialog.getFirstCardinality();
                String cardSecondClass = dialog.getSecondCardinality();

                observer.onAssociationFaibleCreate(firstClass, secondClass, cardFirstClass, cardSecondClass);
            }
        });
    }
}
