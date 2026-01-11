package Controllers;

import java.util.List;
import java.util.Set;

public interface toolPanelObserver {
    void onClassCreate(String className);
    void onInheritanceCreate(String parent, String child);
    void onAggregationCreate(String firstClass, String secondClass, String cardFirstClass, String cardSecondClass);
    void onCompositionCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    void onAssociationForteCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    void onAssociationFaibleCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
    List<String> getClassNames();
    Set<String> getClassesWithParent();
    void generateCode();
}
