package Model;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    private String name;
    private List<Attribute> attributes;
    private List<Method> methods;
    private List<String> parents;
    private List<Relationship> relationships;
    
    public ClassModel(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }
    
    public void addAttribute(String type, String name) {
        attributes.add(new Attribute(type, name));
    }
    
    public void addMethod(String returnType, String name, List<String[]> args) {
        methods.add(new Method(returnType, name, args));
    }
    
    public void addParent(String parentName) {
        parents.add(parentName);
    }
    
    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }
    
    public String getName() { return name; }
    public List<Attribute> getAttributes() { return attributes; }
    public List<Method> getMethods() { return methods; }
    public List<String> getParents() { return parents; }
    public List<Relationship> getRelationships() { return relationships; }
    
    public static class Attribute {
        private String type;
        private String name;
        
        public Attribute(String type, String name) {
            this.type = type;
            this.name = name;
        }
        
        public String getType() { return type; }
        public String getName() { return name; }
    }
    
    public static class Method {
        private String returnType;
        private String name;
        private List<String[]> arguments;
        
        public Method(String returnType, String name, List<String[]> arguments) {
            this.returnType = returnType;
            this.name = name;
            this.arguments = arguments != null ? arguments : new ArrayList<>();
        }
        
        public String getReturnType() { return returnType; }
        public String getName() { return name; }
        public List<String[]> getArguments() { return arguments; }
    }
}
