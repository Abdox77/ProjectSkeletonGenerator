package View.toolPanel.Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class inheritanceDialog extends JDialog{
    private JComboBox<String> parentClassCombo;
    private JComboBox<String> childClassCombo;
    private boolean confirmed = false;

    public inheritanceDialog(Frame parent, List<String> classNames) {
        super(parent, "Create Inheritance", true);
        initComponent(classNames);
        setLocationRelativeTo(parent);
    }

    private void initComponent(List<String> classNames) {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Parent Class :"));
        parentClassCombo = new JComboBox<>(classNames.toArray(new String[0]));
        formPanel.add(parentClassCombo);

        formPanel.add(new JLabel("Child Class:"));
        childClassCombo = new JComboBox<>(classNames.toArray(new String[0]));
        formPanel.add(childClassCombo);        
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    private boolean validateInput()
    {
        if (parentClassCombo.getSelectedItem() == null || 
            childClassCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select both parent and child classes", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String parent = (String) parentClassCombo.getSelectedItem();
        String child = (String) childClassCombo.getSelectedItem();
        
        if (parent.equals(child)) {
            JOptionPane.showMessageDialog(this, 
                "Parent and child classes must be different", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getParentClass() {
        return (String) parentClassCombo.getSelectedItem();
    }

    public String getChildClass() {
        return (String) childClassCombo.getSelectedItem();
    }
}
