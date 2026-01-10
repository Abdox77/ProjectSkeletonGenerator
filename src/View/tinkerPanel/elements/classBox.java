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
    private ArrayList<JLabel> attributes;
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
        JMenuItem addMethod = getMethodJMenuItem();
        JMenuItem deleteClass = getDeleteJMenuItem();

        contextMenu.add(addAttr);
        contextMenu.add(addMethod);
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

    private JMenuItem getAttrJMenuItem() {
        JMenuItem addAttr = new JMenuItem("Add Attribute");

        addAttr.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            AttributeDialog dialog = new AttributeDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String attr = dialog.getAttributeName();
                String type = dialog.getAttributeType();
                attr = attr.trim();
                if (attr.isBlank())
                    return;
                createNewAttr(type, attr);
            }
        });
        return addAttr;
    }

    private JMenuItem getMethodJMenuItem() {
        JMenuItem addMethod = new JMenuItem("Add Method");
        addMethod.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            MethodDialog dialog = new MethodDialog(parentFrame);
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
            sb.append(returnType.toUpperCase());
            sb.append(" ");
        }
        sb.append(methodName);
        sb.append(" (");
        int i = 0;
        for(String[] tmp : methodArgs) {
            ++i;
            sb.append(tmp[0]);
            sb.append(": ");
            sb.append(tmp[1].toUpperCase());
            if (i + 1 < methodArgs.size()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
