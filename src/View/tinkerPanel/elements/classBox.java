package View.tinkerPanel.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import View.tinkerPanel.DragHandler;
import View.tinkerPanel.tools.AttributeDialog;
import View.tinkerPanel.tools.MethodDialog;

public class classBox extends JPanel {
    private JPopupMenu contextMenu;
    private JLabel nameLabel;
    private JPanel groupPanel;
    private JPanel attributePanel;
    private JPanel methodsPanel;
    private final Color CLASSBOX_COLOR = Color.LIGHT_GRAY;
    public DragHandler dragHandler;
    private List<String[]> attributesList;
    private List<MethodData> methodsList;


    public classBox(String title, int x, int y) {
        setName(title);
        attributesList = new ArrayList<>();
        methodsList = new ArrayList<>();
        setLayout(x, y);
        addClassName(title);
        attributePanel = defaultClassBoxPanel(1);
        methodsPanel = defaultClassBoxPanel(0);
        addGroupPanel();
        initContextMenu();
    }

    private void setLayout(int x, int y) {
        setLayout(new BorderLayout());
        setBounds(x, y, 200, 200);
        setBackground(CLASSBOX_COLOR);
        setPreferredSize(new Dimension(200, 200));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void addClassName(String name) {
        nameLabel = new JLabel(name);
        nameLabel.setOpaque(true);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setBackground(CLASSBOX_COLOR);
        nameLabel.setPreferredSize(new Dimension(200, 30));
        nameLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        add(nameLabel, BorderLayout.NORTH);
    }

    private void addGroupPanel() {
        groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setOpaque(true);
        groupPanel.setBackground(CLASSBOX_COLOR);
        groupPanel.add(attributePanel);
        groupPanel.add(methodsPanel);
        add(groupPanel, BorderLayout.CENTER);
    }


    private JPanel defaultClassBoxPanel(int hasBottom) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CLASSBOX_COLOR);
        panel.setOpaque(true);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, hasBottom, 0, Color.BLACK));
        return panel;
    }

    private void initContextMenu() {
        contextMenu = new JPopupMenu();

        JMenuItem addAttr = getAttrJMenuItem();
        JMenuItem editAttr = getEditAttrJMenuItem();
        JMenuItem addMethod = getMethodJMenuItem();
        JMenuItem editMethod = getEditMethodJMenuItem();
        JMenuItem deleteClass = getDeleteJMenuItem();

        contextMenu.add(addAttr);
        contextMenu.add(editAttr);
        contextMenu.addSeparator();
        contextMenu.add(addMethod);
        contextMenu.add(editMethod);
        contextMenu.addSeparator();
        contextMenu.add(deleteClass);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private void displayPopUp(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    requestFocusInWindow();
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            @Override public void mousePressed(MouseEvent e) { displayPopUp(e); }
            @Override public void mouseReleased(MouseEvent e) { displayPopUp(e); }
        };
        addMouseListener(mouseAdapter);
    }

    private List<String> getAvailableClassNames() {
        List<String> classNames = new ArrayList<>();
        Container parent = getParent();
        if (parent != null) {
            try {
                java.lang.reflect.Method method = parent.getClass().getMethod("getClassNames");
                @SuppressWarnings("unchecked")
                List<String> names = (List<String>) method.invoke(parent);
                classNames.addAll(names);
            } catch (Exception ex) {
            }
        }
        return classNames;
    }

    private JMenuItem getAttrJMenuItem() {
        JMenuItem addAttr = new JMenuItem("Add Attribute(s)");

        addAttr.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            List<String> classNames = getAvailableClassNames();
            AttributeDialog dialog = new AttributeDialog(parentFrame, classNames);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                List<String[]> newAttributes = dialog.getAttributesList();
                for (String[] attr : newAttributes) {
                    String type = attr[0];
                    String name = attr[1];
                    if (!name.isBlank()) {
                        createNewAttr(type, name);
                    }
                }
            }
        });
        return addAttr;
    }

    private JMenuItem getEditAttrJMenuItem() {
        JMenuItem editAttr = new JMenuItem("Edit/Delete Attributes");
        editAttr.addActionListener(e -> {
            if (attributesList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No attributes to edit.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            String[] attrNames = new String[attributesList.size()];
            for (int i = 0; i < attributesList.size(); i++) {
                attrNames[i] = attributesList.get(i)[1] + ": " + attributesList.get(i)[0];
            }
            
            String selected = (String) JOptionPane.showInputDialog(
                parentFrame,
                "Select attribute to edit or delete:",
                "Edit Attributes",
                JOptionPane.PLAIN_MESSAGE,
                null,
                attrNames,
                attrNames[0]
            );
            
            if (selected != null) {
                int index = -1;
                for (int i = 0; i < attrNames.length; i++) {
                    if (attrNames[i].equals(selected)) {
                        index = i;
                        break;
                    }
                }
                
                if (index >= 0) {
                    String[] options = {"Edit", "Delete", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(
                        parentFrame,
                        "What do you want to do with '" + selected + "'?",
                        "Edit/Delete Attribute",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );
                    
                    if (choice == 0) {
                        editAttribute(index, parentFrame);
                    } else if (choice == 1) {
                        deleteAttribute(index);
                    }
                }
            }
        });
        return editAttr;
    }

    private void editAttribute(int index, Frame parentFrame) {
        String[] attr = attributesList.get(index);
        String currentType = attr[0];
        String currentName = attr[1];
        
        boolean isList = currentType.startsWith("List<");
        String baseType = isList ? currentType.substring(5, currentType.length() - 1) : currentType;
        
        JTextField nameField = new JTextField(currentName, 15);
        List<String> types = new ArrayList<>();
        types.add("int");
        types.add("char");
        types.add("string");
        types.add("boolean");
        types.add("double");
        types.add("float");
        types.add("long");
        types.addAll(getAvailableClassNames());
        
        javax.swing.JComboBox<String> typeCombo = new javax.swing.JComboBox<>(types.toArray(new String[0]));
        typeCombo.setEditable(true);
        typeCombo.setSelectedItem(baseType);
        
        javax.swing.JCheckBox listCheck = new javax.swing.JCheckBox("Is List", isList);
        
        JPanel panel = new JPanel(new java.awt.GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel(""));
        panel.add(listCheck);
        
        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit Attribute", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newType = (String) typeCombo.getSelectedItem();
            if (listCheck.isSelected()) {
                newType = "List<" + newType + ">";
            }
            
            if (!newName.isBlank() && newType != null) {
                attributesList.set(index, new String[]{newType, newName});
                refreshAttributePanel();
            }
        }
    }

    private void deleteAttribute(int index) {
        attributesList.remove(index);
        refreshAttributePanel();
    }

    private void refreshAttributePanel() {
        attributePanel.removeAll();
        for (String[] attr : attributesList) {
            JLabel label = new JLabel("- " + attr[1] + ": " + attr[0].toUpperCase());
            label.setOpaque(true);
            label.setBackground(CLASSBOX_COLOR);
            label.setPreferredSize(new Dimension(200, 20));
            label.setMaximumSize(label.getPreferredSize());
            attributePanel.add(label);
        }
        attributePanel.revalidate();
        attributePanel.repaint();
    }

    private JMenuItem getMethodJMenuItem() {
        JMenuItem addMethod = new JMenuItem("Add Method");
        addMethod.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            List<String> classNames = getAvailableClassNames();
            MethodDialog dialog = new MethodDialog(parentFrame, classNames);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                String returnType = dialog.getReturnType();
                String methodName = dialog.getMethodName();
                List<String[]> methodArgs = dialog.getMethodArgs();
                createNewMethod(returnType, methodName, methodArgs);
            }
        });
        return addMethod;
    }

    private JMenuItem getEditMethodJMenuItem() {
        JMenuItem editMethod = new JMenuItem("Edit/Delete Methods");
        editMethod.addActionListener(e -> {
            if (methodsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No methods to edit.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            String[] methodNames = new String[methodsList.size()];
            for (int i = 0; i < methodsList.size(); i++) {
                MethodData md = methodsList.get(i);
                methodNames[i] = md.name + "() : " + md.returnType;
            }
            
            String selected = (String) JOptionPane.showInputDialog(
                parentFrame,
                "Select method to edit or delete:",
                "Edit Methods",
                JOptionPane.PLAIN_MESSAGE,
                null,
                methodNames,
                methodNames[0]
            );
            
            if (selected != null) {
                int index = -1;
                for (int i = 0; i < methodNames.length; i++) {
                    if (methodNames[i].equals(selected)) {
                        index = i;
                        break;
                    }
                }
                
                if (index >= 0) {
                    String[] options = {"Edit", "Delete", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(
                        parentFrame,
                        "What do you want to do with '" + selected + "'?",
                        "Edit/Delete Method",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );
                    
                    if (choice == 0) {
                        editMethod(index, parentFrame);
                    } else if (choice == 1) {
                        deleteMethod(index);
                    }
                }
            }
        });
        return editMethod;
    }

    private void editMethod(int index, Frame parentFrame) {
        MethodData md = methodsList.get(index);
        
        boolean isReturnList = md.returnType.startsWith("List<");
        String baseReturnType = isReturnList ? md.returnType.substring(5, md.returnType.length() - 1) : md.returnType;
        
        List<String> types = new ArrayList<>();
        types.add("void");
        types.add("int");
        types.add("char");
        types.add("string");
        types.add("boolean");
        types.add("double");
        types.add("float");
        types.add("long");
        types.addAll(getAvailableClassNames());
        
        JTextField nameField = new JTextField(md.name, 15);
        javax.swing.JComboBox<String> returnTypeCombo = new javax.swing.JComboBox<>(types.toArray(new String[0]));
        returnTypeCombo.setEditable(true);
        returnTypeCombo.setSelectedItem(baseReturnType);
        javax.swing.JCheckBox returnListCheck = new javax.swing.JCheckBox("Is List", isReturnList);
        
        StringBuilder argsStr = new StringBuilder();
        for (int i = 0; i < md.arguments.size(); i++) {
            if (i > 0) argsStr.append(", ");
            argsStr.append(md.arguments.get(i)[0]).append(": ").append(md.arguments.get(i)[1]);
        }
        JTextField argsField = new JTextField(argsStr.toString(), 20);
        argsField.setToolTipText("Format: name1: type1, name2: type2");
        
        JPanel panel = new JPanel(new java.awt.GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Method Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Return Type:"));
        panel.add(returnTypeCombo);
        panel.add(new JLabel(""));
        panel.add(returnListCheck);
        panel.add(new JLabel("Arguments:"));
        panel.add(argsField);
        
        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit Method", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newReturnType = (String) returnTypeCombo.getSelectedItem();
            if (returnListCheck.isSelected() && !newReturnType.equals("void")) {
                newReturnType = "List<" + newReturnType + ">";
            }
            
            List<String[]> newArgs = new ArrayList<>();
            String argsText = argsField.getText().trim();
            if (!argsText.isEmpty()) {
                String[] argParts = argsText.split(",");
                for (String arg : argParts) {
                    String[] parts = arg.trim().split(":");
                    if (parts.length == 2) {
                        newArgs.add(new String[]{parts[0].trim(), parts[1].trim()});
                    }
                }
            }
            
            if (!newName.isBlank() && newReturnType != null) {
                methodsList.set(index, new MethodData(newReturnType, newName, newArgs));
                refreshMethodsPanel();
            }
        }
    }

    private void deleteMethod(int index) {
        methodsList.remove(index);
        refreshMethodsPanel();
    }

    private void refreshMethodsPanel() {
        methodsPanel.removeAll();
        for (MethodData md : methodsList) {
            JLabel label = new JLabel(buildMethodString(md.returnType, md.name, md.arguments));
            label.setOpaque(true);
            label.setBackground(CLASSBOX_COLOR);
            label.setPreferredSize(new Dimension(200, 20));
            label.setMaximumSize(label.getPreferredSize());
            methodsPanel.add(label);
        }
        methodsPanel.revalidate();
        methodsPanel.repaint();
    }

    private JMenuItem getDeleteJMenuItem() {
        JMenuItem deleteClass = new JMenuItem("Delete Class");
        deleteClass.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + getName() + "'?\nThis will also remove all its relations.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                Container parent = getParent();
                if (parent != null) {
                    java.lang.reflect.Method method;
                    try {
                        method = parent.getClass().getMethod("deleteClassBox", classBox.class);
                        method.invoke(parent, this);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        return deleteClass;
    }

    private void createNewAttr(String type, String name) {
        attributesList.add(new String[]{type, name});
        JLabel attr = new JLabel("- " + name + ": " + type.toUpperCase());
        attr.setOpaque(true);
        attr.setBackground(CLASSBOX_COLOR);
        attr.setPreferredSize(new Dimension(200, 20));
        attr.setMaximumSize(attr.getPreferredSize());
        attributePanel.add(attr);
        attributePanel.revalidate();
        attributePanel.repaint();
    }

    private void createNewMethod(String returnType, String methodName, List<String[]> methodArgs) {
        if (returnType  == null || methodName == null || methodName.isBlank()) {
            System.out.println("the return type is invalid");
            return;
        }
        methodsList.add(new MethodData(returnType, methodName, methodArgs));
        JLabel attr = new JLabel();
        attr.setText(buildMethodString(returnType, methodName, methodArgs));
        attr.setOpaque(true);
        attr.setBackground(CLASSBOX_COLOR);
        attr.setPreferredSize(new Dimension(200, 20));
        attr.setMaximumSize(attr.getPreferredSize());
        methodsPanel.add(attr);
        methodsPanel.revalidate();
        methodsPanel.repaint();
    }

    public List<String[]> getAttributesList() {
        return new ArrayList<>(attributesList);
    }
    
    public List<MethodData> getMethodsList() {
        return new ArrayList<>(methodsList);
    }

    public static class MethodData {
        public String returnType;
        public String name;
        public List<String[]> arguments;
        
        public MethodData(String returnType, String name, List<String[]> arguments) {
            this.returnType = returnType;
            this.name = name;
            this.arguments = new ArrayList<>(arguments);
        }
    }

    private String buildMethodString(String returnType, String methodName, List<String[]> methodArgs) {
        StringBuilder sb = new StringBuilder();
        sb.append("+ ");
        returnType = returnType.toUpperCase();
        if (!returnType.equals("VOID")) {
            sb.append(returnType);
            sb.append(" ");
        }
        sb.append(methodName);
        sb.append(" (");
        int i = 0;
        for(String[] tmp : methodArgs) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(tmp[0]);
            sb.append(": ");
            sb.append(tmp[1].toUpperCase());
            i++;
        }
        sb.append(")");
        return sb.toString();
    }
}
