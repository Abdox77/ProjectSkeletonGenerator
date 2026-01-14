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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class AttributeDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> typeCombo;
    private JCheckBox isListCheckBox;
    private DefaultListModel<String> attrsListModel;
    private JList<String> attrsList;
    private List<String[]> attributesList;
    private boolean confirmed = false;

    public AttributeDialog(Frame parent) {
        this(parent, new ArrayList<>());
    }

    public AttributeDialog(Frame parent, List<String> classNames) {
        super(parent, "Add Attributes", true);
        attributesList = new ArrayList<>();
        initComponents(classNames);
        setLocationRelativeTo(parent);
    }

    private void initComponents(List<String> classNames) {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Attribute Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        List<String> types = new ArrayList<>();
        types.add("int");
        types.add("char");
        types.add("string");
        types.add("boolean");
        types.add("double");
        types.add("float");
        types.add("long");
        for (String className : classNames) {
            if (!types.contains(className)) {
                types.add(className);
            }
        }
        typeCombo = new JComboBox<>(types.toArray(new String[0]));
        typeCombo.setEditable(true);
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        isListCheckBox = new JCheckBox("Is List (List<Type>)");
        formPanel.add(isListCheckBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addAttribute());
        formPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        JLabel listLabel = new JLabel("Attributes to add:");
        listLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        formPanel.add(listLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        attrsListModel = new DefaultListModel<>();
        attrsList = new JList<>(attrsListModel);
        JScrollPane scrollPane = new JScrollPane(attrsList);
        scrollPane.setPreferredSize(new Dimension(250, 120));
        formPanel.add(scrollPane, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeAttribute());
        formPanel.add(removeButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Add All");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (!nameField.getText().trim().isBlank()) {
                addAttribute();
            }
            if (!attributesList.isEmpty()) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please add at least one attribute",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
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
        setMinimumSize(new Dimension(400, 350));
    }

    private void addAttribute() {
        String name = nameField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an attribute name",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type == null || type.trim().isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Please select or enter a type",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (String[] attr : attributesList) {
            if (attr[1].equals(name)) {
                JOptionPane.showMessageDialog(this,
                        "Attribute '" + name + "' already added",
                        "Duplicate Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String finalType = type.trim();
        if (isListCheckBox.isSelected()) {
            finalType = "List<" + finalType + ">";
        }

        attributesList.add(new String[]{finalType, name});
        attrsListModel.addElement("- " + name + ": " + finalType.toUpperCase());

        nameField.setText("");
        isListCheckBox.setSelected(false);
        nameField.requestFocus();
    }

    private void removeAttribute() {
        int selectedIndex = attrsList.getSelectedIndex();
        if (selectedIndex != -1) {
            attrsListModel.remove(selectedIndex);
            attributesList.remove(selectedIndex);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getAttributeName() {
        if (!attributesList.isEmpty()) {
            return attributesList.get(0)[1];
        }
        return "";
    }

    public String getAttributeType() {
        if (!attributesList.isEmpty()) {
            return attributesList.get(0)[0];
        }
        return "int";
    }

    public List<String[]> getAttributesList() {
        return new ArrayList<>(attributesList);
    }
}
