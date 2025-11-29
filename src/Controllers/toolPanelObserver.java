package Controllers;

public interface toolPanelObserver {
    public void onClassCreate(String className);
    public void onInheritanceCreate(String parent, String child);
    public void onAggregationCreate(String firstClass, String secondClass, String cardFirstClass, String cardSecondClass);
    public void onCompositionCreate(String firstClass, String secondClass, String cardFirst, String cardSecond);
}
