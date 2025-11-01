package View.tinkerPanel.elements;

import View.tinkerPanel.DragHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class classBox extends JPanel {
    public DragHandler dragHandler;
    private JLabel nameLabel;
    private JPanel groupPanel;
    private JPanel attributePanel;
    private JPanel methodsPanel;
//    private ArrayList<JLabel> attributes = new ArrayList<JPanel>();
    private final Color CLASSBOX_COLOR = Color.LIGHT_GRAY;

    private void setLayout() {
        setLayout(new BorderLayout());
        setBounds(50, 50, 200, 200);
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
//        groupPanel.add(methodsPanel);
        add(groupPanel, BorderLayout.CENTER);
    }

    private void addAttributePanel() {
        attributePanel = new JPanel();
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributePanel.setBackground(CLASSBOX_COLOR);
        attributePanel.setOpaque(true);
        attributePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        attributePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void addMethodsPanel() {

    }

    private void createNewAttr(String type, String name) {
        JLabel attr = new JLabel(name + ": " + type.toUpperCase());
        attr.setPreferredSize(new Dimension(200, 20));
        attr.setBackground(CLASSBOX_COLOR);
        attr.setOpaque(true);
        attr.setMaximumSize(attr.getPreferredSize());
        attributePanel.add(attr);
    }

    private void addDragHandler() {
//        dragHandler = new DragHandler(this);
    }

    public classBox(String title) {
        setLayout();
        addClassName(title);
        addAttributePanel();
        addGroupPanel();
        createNewAttr("int", "a");
        createNewAttr("char", "a");
        addDragHandler();
    }


    public void addAttribute(String attrName, String attrType) {

    }
}
