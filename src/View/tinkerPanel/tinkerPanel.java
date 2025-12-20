package View.tinkerPanel;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import Controllers.toolPanelObserver;
import View.tinkerPanel.elements.aggregation;
import View.tinkerPanel.elements.classBox;
import View.tinkerPanel.elements.composition;
import View.tinkerPanel.elements.inheritance;

public class tinkerPanel extends JPanel implements toolPanelObserver
{
    private static final int CLASS_WIDTH = 200;
    private static final int CLASS_HEIGHT = 200;
    private static final int MARGIN = 50;
    private final Color TINKERPANEL_BACKGROUND = Color.WHITE;
    private final List<classBox> classBoxList;
    private final List<inheritance>  inheritanceList;
    private final List<aggregation> aggregationList;
    private final List<composition> compositionList;
    private final Random random;



    public tinkerPanel() {
        classBoxList = new ArrayList<classBox>();
        inheritanceList = new ArrayList<inheritance>();
        aggregationList = new ArrayList<aggregation>();
        compositionList = new ArrayList<composition>();
        random = new Random();

        classBox child = createClassBox("h");
        classBox parent = createClassBox("w");
        addClassBox(child);
        addClassBox(parent);

        this.setLayout(null);
        this.setBackground(TINKERPANEL_BACKGROUND);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateRelationBounds();
            }
        });
    }

    private void updateRelationBounds() {
        Rectangle bounds = getBounds();
        for (inheritance inh : inheritanceList) {
            inh.setBounds(0, 0, bounds.width, bounds.height);
        }
        for (aggregation agg : aggregationList) {
            agg.setBounds(0, 0, bounds.width, bounds.height);
        }
        repaint();
    }

    private void addClassBox(classBox classBox)
    {
        for (classBox box : classBoxList)
        {
            if (box.getName().equals(classBox.getName())) return;
        }
        classBoxList.add(classBox);
        add(classBox);

        classBox.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                repaintRelations();
            }
        });

        revalidate();
        repaint();
        classBox.dragHandler = new DragHandler(classBox);
    }

    private void repaintRelations() {
        for (inheritance inh : inheritanceList) {
            inh.repaint();
        }
        for (aggregation agg : aggregationList) {
            agg.repaint();
        }
    }

    private classBox createClassBox(String name)
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        
        if (panelWidth == 0) panelWidth = 800;
        if (panelHeight == 0) panelHeight = 600;
        
        int maxX = panelWidth - CLASS_WIDTH - MARGIN;
        int maxY = panelHeight - CLASS_HEIGHT - MARGIN;
        
        int x = MARGIN + random.nextInt(Math.max(1, maxX - MARGIN));
        int y = MARGIN + random.nextInt(Math.max(1, maxY - MARGIN));
        return new classBox(name, x, y);
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
        Rectangle bounds = getBounds();
        a.setBounds(0, 0, bounds.width, bounds.height);
        aggregationList.add(a);
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
        classBox _first = getClassBox(firstClass);
        classBox _second = getClassBox(secondClass);

        // i should also check here the cardinalities

        if(_second == null || _first == null) return;
        addComposition(createNewComposition(_first, _second, cardFirst, cardSecond));
    }

    private composition createNewComposition(classBox _first, classBox _second, String cardFirst, String cardSecond) {
        return new composition(_first, _second, cardFirst, cardSecond);
    }

    private void addComposition(composition c) {
        Rectangle bounds = getBounds();
        c.setBounds(0, 0, bounds.width, bounds.height);
        compositionList.add(c);
        add(c);
        revalidate();
        repaint();
    }

    @Override
    public void onInheritanceCreate(String parent, String child) {
        System.out.println("i got notified inheritance class");

        classBox _parent = getClassBox(parent);
        classBox _child = getClassBox(child);

        if(parent == null || child == null) return;
        addInheritance(createInheritance(_parent, _child));
    }

    @Override
    public void generateCode()
    {
        System.out.println("i got notified generation code");
    }

    @Override
    public List<String> getClassNames() {
        List<String> names = new ArrayList<>();
        for (classBox box : classBoxList) {
            names.add(box.getName());
        }
        return names;
    }

    private void addInheritance(inheritance inheritance) {
        Rectangle bounds = getBounds();
        inheritance.setBounds(0, 0, bounds.width, bounds.height);
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
