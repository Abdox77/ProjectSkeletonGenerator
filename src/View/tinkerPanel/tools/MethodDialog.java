package View.tinkerPanel.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MethodDialog extends JDialog {
    private JTextField methodNameField;
    private JComboBox<String> returnTypeCombo;
    private JTextField argNameField;
    private JComboBox<String> argTypeCombo;
    private DefaultListModel<String> argsListModel;
    private JList<String> argsList;
    private List<String[]> methodArgs;
    private boolean confirmed = false;

    public MethodDialog(Frame parent) {
        super(parent, "Add Method", true);
        methodArgs = new ArrayList<>();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Method Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        methodNameField = new JTextField(20);
        formPanel.add(methodNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Return Type:"), gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        returnTypeCombo = new JComboBox<>(new String[]{"void", "int", "char", "string", "boolean"});
        formPanel.add(returnTypeCombo, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel argsLabel = new JLabel("Arguments:");
        argsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        formPanel.add(argsLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Arg Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        argNameField = new JTextField(10);
        formPanel.add(argNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Arg Type:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        argTypeCombo = new JComboBox<>(new String[]{"int", "char", "string", "boolean"});
        formPanel.add(argTypeCombo, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        JButton addArgButton = new JButton("Add Arg");
        addArgButton.addActionListener(e -> addArgument());
        formPanel.add(addArgButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        argsListModel = new DefaultListModel<>();
        argsList = new JList<>(argsListModel);
        JScrollPane scrollPane = new JScrollPane(argsList);
        scrollPane.setPreferredSize(new Dimension(250, 100));
        formPanel.add(scrollPane, gbc);

        gbc.gridx = 2; 
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JButton removeArgButton = new JButton("Remove");
        removeArgButton.addActionListener(e -> removeArgument());
        formPanel.add(removeArgButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Add Method");
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
        setMinimumSize(new Dimension(400, 400));
    }

    private void addArgument() {
        String argName = argNameField.getText().trim();
        String argType = (String) argTypeCombo.getSelectedItem();

        if (argName.isBlank()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter an argument name", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        methodArgs.add(new String[]{argName, argType});
        argsListModel.addElement(argName + ": " + argType);
        
        argNameField.setText("");
        argNameField.requestFocus();
    }

    private void removeArgument() {
        int selectedIndex = argsList.getSelectedIndex();
        if (selectedIndex != -1) {
            argsListModel.remove(selectedIndex);
            methodArgs.remove(selectedIndex);
        }
    }

    private boolean validateInput() {
        String methodName = methodNameField.getText().trim();
        
        if (methodName.isBlank()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a method name", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getMethodName() {
        return methodNameField.getText().trim();
    }

    public String getReturnType() {
        return (String) returnTypeCombo.getSelectedItem();
    }

    public List<String[]> getMethodArgs() {
        return new ArrayList<>(methodArgs);
    }
}
