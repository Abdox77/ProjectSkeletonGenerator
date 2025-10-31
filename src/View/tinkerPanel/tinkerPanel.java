package View.tinkerPanel;

import View.tinkerPanel.elements.classBox;
import Controllers.toolPanelObserver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class tinkerPanel extends JPanel implements toolPanelObserver {
    private final Color TINKERPANEL_BACKGROUND = Color.WHITE;
    private final List<classBox> classBoxList;


    public tinkerPanel() {
        classBoxList = new ArrayList<classBox>();
        this.setLayout(null);
        this.setBackground(TINKERPANEL_BACKGROUND);
    }

    public void addClassBox(classBox classBox) {
        classBoxList.add(classBox);

        // here i should perform checks Naming and the existing of a similar class ....

        this.classBoxList.add(classBox);
        this.add(classBox);
        this.repaint();
    }

    private classBox createClassBox(String name) {
        return new classBox(name);
    }

    @Override
    public void onClassCreate(String event) {
        System.out.println("i got notified class");
        this.addClassBox(this.createClassBox(event));
    }
}
