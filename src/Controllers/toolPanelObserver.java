package Controllers;

public interface toolPanelObserver {
    public void onClassCreate(String className);
    public void onInheritanceCreate(String parent, String child);
}
