package View.tinkerPanel.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

import View.tinkerPanel.DragHandler;

public class classBox extends JPanel {
    private JPopupMenu contextMenu;
    private JLabel nameLabel;
    private JPanel groupPanel;
    private JPanel attributePanel;
    private JPanel methodsPanel;
    private ArrayList<JLabel> attributes;
    private final Color CLASSBOX_COLOR = Color.LIGHT_GRAY;
    public DragHandler dragHandler;


    public classBox(String title, int x, int y) {
        setName(title);
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

        contextMenu.add(addAttr);
        contextMenu.add(addMethod);
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
            String input =  JOptionPane.showInputDialog(this, "Attribute (name:type)", "Add Attribute");
            if (input == null || input.isBlank()) {
                return;
            }
            String[] attr = input.split(":");
            if (attr.length != 2) {
                return;
            }
            String name = attr[0].trim();
            String type = attr[1].trim();
            if (checkInputType(type) && !name.isBlank()) {
                createNewAttr(type, name);
            }
        });
        return addAttr;
    }

    private JMenuItem getMethodJMenuItem() {
        JMenuItem addMethod = new JMenuItem("Add Method");
        addMethod.addActionListener(e -> {
            String input =  JOptionPane.showInputDialog(this, "returnType methodName (arg1:type, ...)", "Add Method");
            if (input == null || input.isBlank()) {
                return;
            }
            String[] inputs = input.split("\\(");
            if (inputs.length != 2) {
                return;
            }
            String[] signature = inputs[0].split(" ");
            if (signature.length != 2) {
                System.out.println(signature.length);
                System.out.println("invalid signature");
                System.out.print(inputs[0]);
                return;
            }
            String returnType = signature[0].trim();
            String methodName = signature[1].trim();
            if (signature.length != 2) {
                return;
            }

            inputs[1] = inputs[1].trim();
            if (inputs[1].charAt(inputs[1].length() - 1) != ')') {
                System.out.println("the input doesn't end with a (");
                return;
            }
            inputs[1] = inputs[1].substring(0, inputs[1].length() - 1);
            String[] args = inputs[1].split(",");
            List<String []> methodArgs = new ArrayList<>();
            for(String arg : args) {
                String []tmp = arg.split(":");
                if (!isValidArg(tmp)) {
                    System.out.println("the argument is invalid " + arg);
                    return;
                }
                methodArgs.add(tmp);
            }
            createNewMethod(returnType, methodName, methodArgs);
        });
        return addMethod;
    }


    private void createNewAttr(String type, String name) {
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


    private boolean isValidArg(String[] args) {
        if (args.length != 2) {
            return false;
        }
        String name = args[0].trim();
        String type = args[1].trim();
        return (checkInputType(type) && !name.isBlank());
    }

    private boolean checkInputType(String type) {
        type = type.toLowerCase();
        return (type.equals("int") || type.equals("char") || type.equals("string") || type.equals("boolean"));
    }
}
