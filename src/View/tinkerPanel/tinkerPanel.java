package View.tinkerPanel;


import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controllers.toolPanelObserver;
import Model.ClassModel;
import Model.CodeGenerator;
import Model.Relationship;
import View.tinkerPanel.elements.aggregation;
import View.tinkerPanel.elements.associationFaible;
import View.tinkerPanel.elements.associationForte;
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
    private final List<associationForte> associationForteList;
    private final List<associationFaible> associationFaibleList;
    private final Random random;



    public tinkerPanel() {
        classBoxList = new ArrayList<classBox>();
        inheritanceList = new ArrayList<inheritance>();
        aggregationList = new ArrayList<aggregation>();
        compositionList = new ArrayList<composition>();
        associationForteList = new ArrayList<associationForte>();
        associationFaibleList = new ArrayList<associationFaible>();
        random = new Random();

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
        for (composition comp : compositionList) {
            comp.setBounds(0, 0, bounds.width, bounds.height);
        }
        for (associationForte assoc : associationForteList) {
            assoc.setBounds(0, 0, bounds.width, bounds.height);
        }
        for (associationFaible assoc : associationFaibleList) {
            assoc.setBounds(0, 0, bounds.width, bounds.height);
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
        for (composition comp : compositionList) {
            comp.repaint();
        }
        for (associationForte assoc : associationForteList) {
            assoc.repaint();
        }
        for (associationFaible assoc : associationFaibleList) {
            assoc.repaint();
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

        if(_second == null || _first == null) return;
        addAggregation(createNewAggregation(_first, _second, cardFirst, cardSecond));
    }

    private int countRelationsBetween(classBox a, classBox b) {
        int count = 0;
        for (aggregation agg : aggregationList) {
            if ((agg.getFrom() == a && agg.getTo() == b) || (agg.getFrom() == b && agg.getTo() == a)) {
                count++;
            }
        }
        for (composition comp : compositionList) {
            if ((comp.getFrom() == a && comp.getTo() == b) || (comp.getFrom() == b && comp.getTo() == a)) {
                count++;
            }
        }
        for (inheritance inh : inheritanceList) {
            if ((inh.getChild() == a && inh.getParent() == b) || (inh.getChild() == b && inh.getParent() == a)) {
                count++;
            }
        }
        for (associationForte assoc : associationForteList) {
            if ((assoc.getFrom() == a && assoc.getTo() == b) || (assoc.getFrom() == b && assoc.getTo() == a)) {
                count++;
            }
        }
        for (associationFaible assoc : associationFaibleList) {
            if ((assoc.getFrom() == a && assoc.getTo() == b) || (assoc.getFrom() == b && assoc.getTo() == a)) {
                count++;
            }
        }
        return count;
    }

    private void addAggregation(aggregation a)
    {
        int index = countRelationsBetween(a.getFrom(), a.getTo());
        a.setRelationIndex(index);
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

        if(_second == null || _first == null) return;
        addComposition(createNewComposition(_first, _second, cardFirst, cardSecond));
    }

    private composition createNewComposition(classBox _first, classBox _second, String cardFirst, String cardSecond) {
        return new composition(_first, _second, cardFirst, cardSecond);
    }

    private void addComposition(composition c) {
        int index = countRelationsBetween(c.getFrom(), c.getTo());
        c.setRelationIndex(index);
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

        if(_parent == null || _child == null) return;
        addInheritance(createInheritance(_parent, _child));
    }

    @Override
    public void onAssociationForteCreate(String firstClass, String secondClass, String cardFirst, String cardSecond) {
        System.out.println("i got notified association forte");

        classBox _first = getClassBox(firstClass);
        classBox _second = getClassBox(secondClass);

        if(_second == null || _first == null) return;
        addAssociationForte(createNewAssociationForte(_first, _second, cardFirst, cardSecond));
    }

    private associationForte createNewAssociationForte(classBox _first, classBox _second, String cardFirst, String cardSecond) {
        return new associationForte(_first, _second, cardFirst, cardSecond);
    }

    private void addAssociationForte(associationForte a) {
        int index = countRelationsBetween(a.getFrom(), a.getTo());
        a.setRelationIndex(index);
        Rectangle bounds = getBounds();
        a.setBounds(0, 0, bounds.width, bounds.height);
        associationForteList.add(a);
        add(a);
        revalidate();
        repaint();
    }

    @Override
    public void onAssociationFaibleCreate(String firstClass, String secondClass, String cardFirst, String cardSecond) {
        System.out.println("i got notified association faible");

        classBox _first = getClassBox(firstClass);
        classBox _second = getClassBox(secondClass);

        if(_second == null || _first == null) return;
        addAssociationFaible(createNewAssociationFaible(_first, _second, cardFirst, cardSecond));
    }

    private associationFaible createNewAssociationFaible(classBox _first, classBox _second, String cardFirst, String cardSecond) {
        return new associationFaible(_first, _second, cardFirst, cardSecond);
    }

    private void addAssociationFaible(associationFaible a) {
        int index = countRelationsBetween(a.getFrom(), a.getTo());
        a.setRelationIndex(index);
        Rectangle bounds = getBounds();
        a.setBounds(0, 0, bounds.width, bounds.height);
        associationFaibleList.add(a);
        add(a);
        revalidate();
        repaint();
    }

    @Override
    public void generateCode()
    {
        System.out.println("i got notified generation code");
        if (classBoxList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No classes to generate code from!",
                    "Empty Diagram",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Output Directory");

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String outputDir = chooser.getSelectedFile().getAbsolutePath();

        try {
            Map<String, ClassModel> models = buildClassModels();
            CodeGenerator generator = new CodeGenerator(models, outputDir);
            generator.generateCode();

            JOptionPane.showMessageDialog(this,
                    "Code generated successfully at:\n" + outputDir,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error generating code: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private Map<String, ClassModel> buildClassModels() {
        Map<String, ClassModel> models = new HashMap<>();

        for (classBox box : classBoxList) {
            ClassModel model = new ClassModel(box.getName());

            for (String[] attr : box.getAttributesList()) {
                model.addAttribute(attr[0], attr[1]);
            }

            for (classBox.MethodData method : box.getMethodsList()) {
                model.addMethod(method.returnType, method.name, method.arguments);
            }

            models.put(box.getName(), model);
        }

        for (inheritance inh : inheritanceList) {
            String childName = inh.getChild().getName();
            String parentName = inh.getParent().getName();

            if (models.containsKey(childName)) {
                models.get(childName).addParent(parentName);
            }
        }

        for (aggregation agg : aggregationList) {
            addRelationshipToModel(models, agg.getFrom(), agg.getTo(),
                    Relationship.RelationType.AGGREGATION, agg.getCardTo());
        }

        for (composition comp : compositionList) {
            addRelationshipToModel(models, comp.getFrom(), comp.getTo(),
                    Relationship.RelationType.COMPOSITION, comp.getCardTo());
        }

        for (associationForte assoc : associationForteList) {
            addRelationshipToModel(models, assoc.getFrom(), assoc.getTo(),
                    Relationship.RelationType.ASSOCIATION_FORTE, assoc.getCardTo());
        }

        for (associationFaible assoc : associationFaibleList) {
            addRelationshipToModel(models, assoc.getFrom(), assoc.getTo(),
                    Relationship.RelationType.ASSOCIATION_FAIBLE, assoc.getCardTo());
        }

        return models;
    }

    private void addRelationshipToModel(Map<String, ClassModel> models, classBox from,
                                        classBox to, Relationship.RelationType type, String cardinality) {
        String fromName = from.getName();
        if (models.containsKey(fromName)) {
            models.get(fromName).addRelationship(
                    new Relationship(type, to.getName(), cardinality)
            );
        }
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
        int index = countRelationsBetween(inheritance.getChild(), inheritance.getParent());
        inheritance.setRelationIndex(index);
        Rectangle bounds = getBounds();
        inheritance.setBounds(0, 0, bounds.width, bounds.height);
        inheritanceList.add(inheritance);
        add(inheritance);
        revalidate();
        repaint();
    }

    private inheritance createInheritance(classBox _parent, classBox _child) {
        return new inheritance(_child, _parent);
    }

    private classBox getClassBox(String className) {
        for (classBox classBox : classBoxList) {
            if (classBox.getName().equals(className)) return classBox;
        }
        return null;
    }

    public void deleteClassBox(classBox box) {
        inheritanceList.removeIf(inh -> {
            if (inh.getChild() == box || inh.getParent() == box) {
                remove(inh);
                return true;
            }
            return false;
        });

        aggregationList.removeIf(agg -> {
            if (agg.getFrom() == box || agg.getTo() == box) {
                remove(agg);
                return true;
            }
            return false;
        });

        compositionList.removeIf(comp -> {
            if (comp.getFrom() == box || comp.getTo() == box) {
                remove(comp);
                return true;
            }
            return false;
        });

        associationForteList.removeIf(assoc -> {
            if (assoc.getFrom() == box || assoc.getTo() == box) {
                remove(assoc);
                return true;
            }
            return false;
        });

        associationFaibleList.removeIf(assoc -> {
            if (assoc.getFrom() == box || assoc.getTo() == box) {
                remove(assoc);
                return true;
            }
            return false;
        });

        classBoxList.remove(box);
        remove(box);
        revalidate();
        repaint();
    }
}
