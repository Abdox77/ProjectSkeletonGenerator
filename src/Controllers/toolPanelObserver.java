package Controllers;

import java.util.List;

public interface toolPanelObserver {
    public void onClassCreate(String className);
    public void onInheritanceCreate(String parent, String child);
    public void onAggregationCreate(String firstClass, String secondClass, String cardFirstClass, String cardSecondClass);
    public void onCompositionCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    public void onAssociationForteCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    public void onAssociationFaibleCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    public List<String> getClassNames();
    public void generateCode();
}

