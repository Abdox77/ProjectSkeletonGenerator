package Model;

public class Relationship {
    private RelationType type;
    private String targetClass;
    private String cardinality;

    public enum RelationType {
        AGGREGATION, COMPOSITION, ASSOCIATION_FORTE, ASSOCIATION_FAIBLE
    }

    public Relationship(RelationType type, String targetClass, String cardinality) {
        this.type = type;
        this.targetClass = targetClass;
        this.cardinality = cardinality;
    }

    public RelationType getType() { return type; }
    public String getTargetClass() { return targetClass; }
    public String getCardinality() { return cardinality; }
}
