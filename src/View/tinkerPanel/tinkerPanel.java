package View.tinkerPanel;

import View.tinkerPanel.elements.aggregation;
import View.tinkerPanel.elements.classBox;
import View.tinkerPanel.elements.inheritance;
import Controllers.toolPanelObserver;
import View.tinkerPanel.tools.cardinality;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class tinkerPanel extends JPanel implements toolPanelObserver
{
    private static final int CLASS_WIDTH = 200;
    private static final int CLASS_HEIGHT = 200;
    private final Color TINKERPANEL_BACKGROUND = Color.WHITE;
    private final List<classBox> classBoxList;
    private final List<inheritance>  inheritanceList;


    public tinkerPanel() {
        classBoxList = new ArrayList<classBox>();
        inheritanceList = new ArrayList<inheritance>();
        this.setLayout(null);
        this.setBackground(TINKERPANEL_BACKGROUND);
        classBox child = createClassBox("h");
        classBox parent = createClassBox("w");
        addClassBox(child);
        addClassBox(parent);
    }

    public void addClassBox(classBox classBox)
    {
        for (classBox box : classBoxList)
        {
            if (box.getName().equals(classBox.getName())) return;
        }
        classBoxList.add(classBox);
        add(classBox);
        revalidate();
        repaint();
        classBox.dragHandler = new DragHandler(classBox);
    }

    private classBox createClassBox(String name)
    {
        return new classBox(name);
    }

    @Override
    public void onClassCreate(String event)
    {
        System.out.println("i got notified class");
        this.addClassBox(this.createClassBox(event));
    }

    @Override
    public void onAggregationCreate(String firstClass, String secondClass, String cardFirst, String cardSecond)
    {
        classBox _first = getClassBox(firstClass);
        classBox _second = getClassBox(secondClass);

        // i should also check here the cardinalities

        if(_second == null || _first == null) return;
        addAggregation(createNewAggregation(_first, _second, cardFirst, cardSecond));
    }

    private void addAggregation(aggregation a)
    {
        a.setBounds(this.getBounds());
        add(a);
        revalidate();
        repaint();
    }

    private aggregation createNewAggregation(classBox _first, classBox _second, String cardFirst, String cardSecond)
    {
        return new aggregation(_first, _second, cardFirst, cardSecond);
    }

    @Override
    public void onCompositionCreate(String firstClass, String secondClass, String cardFirst, String cardSecond)
    {

    }

    @Override
    public void onInheritanceCreate(String parent, String child) {
        System.out.println("i got notified inheritance class");

        classBox _parent = getClassBox(parent);
        classBox _child = getClassBox(child);

        if(parent == null || child == null) return;
        addInheritance(createInheritance(_parent, _child));
    }

    private void addInheritance(inheritance inheritance) {
        inheritance.setBounds(this.getBounds());
        inheritanceList.add(inheritance);
        add(inheritance);
        revalidate();
        repaint();
    }

    private inheritance createInheritance(classBox _parent, classBox _child) {
        return new inheritance(_parent, _child);
    }

    private classBox getClassBox(String className) {
        for (classBox classBox : classBoxList) {
            if (classBox.getName().equals(className)) return classBox;
        }
        return null;
    }
}
