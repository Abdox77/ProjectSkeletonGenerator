package View.toolPanel.Dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class aggregationDialog extends JDialog {
    private JComboBox<String> firstClassCombo;
    private JComboBox<String> secondClassCombo;
    private JComboBox<String> cardFirstCombo;
    private JComboBox<String> cardSecondCombo;
    private boolean confirmed = false;

    public aggregationDialog(Frame parent, List<String> classNames) {
        super(parent, "Create Aggregation", true);
        initComponent(classNames);
        setLocationRelativeTo(parent);
    }

    private void initComponent(List<String> classNames) {
        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("First Class (◇-->):"));
        firstClassCombo = new JComboBox<>(classNames.toArray(new String[0]));
        formPanel.add(firstClassCombo);

        formPanel.add(new JLabel("First Class Cardinality:"));
        cardFirstCombo = new JComboBox<>(new String[]{"0..1", "1..1", "0..*", "1..*"});
        cardFirstCombo.setEditable(true);
        formPanel.add(cardFirstCombo);

        formPanel.add(new JLabel("Second Class (-->):"));
        secondClassCombo = new JComboBox<>(classNames.toArray(new String[0]));
        formPanel.add(secondClassCombo);

        formPanel.add(new JLabel("Second Class Cardinality:"));
        cardSecondCombo = new JComboBox<>(new String[]{"0..1", "1..1", "0..*", "1..*"});
        cardSecondCombo.setEditable(true);
        formPanel.add(cardSecondCombo);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Create");
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
        setMinimumSize(new Dimension(400, 200));
    }

    private boolean validateInput() {
        if (firstClassCombo.getSelectedItem() == null || 
            secondClassCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select both classes", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String card1 = (String) cardFirstCombo.getSelectedItem();
        String card2 = (String) cardSecondCombo.getSelectedItem();
        
        if (card1 == null || card1.isBlank() || card2 == null || card2.isBlank()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter cardinalities", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getFirstClass() {
        return (String) firstClassCombo.getSelectedItem();
    }

    public String getSecondClass() {
        return (String) secondClassCombo.getSelectedItem();
    }

    public String getFirstCardinality() {
        return (String) cardFirstCombo.getSelectedItem();
    }

    public String getSecondCardinality() {
        return (String) cardSecondCombo.getSelectedItem();
    }
}
