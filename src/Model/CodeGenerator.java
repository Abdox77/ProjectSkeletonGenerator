package Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    private Map<String, ClassModel> classModels;
    private String outputDirectory;

    public CodeGenerator(Map<String, ClassModel> classModels, String outputDirectory) {
        this.classModels = classModels;
        this.outputDirectory = outputDirectory;
    }

    public void generateCode() throws IOException {
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (ClassModel classModel : classModels.values()) {
            generateClassFile(classModel);
        }
    }

    private void generateClassFile(ClassModel classModel) throws IOException {
        String fileName = outputDirectory + File.separator + classModel.getName() + ".java";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateClassCode(classModel));
        }
    }

    private String generateClassCode(ClassModel classModel) {
        StringBuilder code = new StringBuilder();
        
        boolean needsListImport = needsListImport(classModel);
        if (needsListImport) {
            code.append("import java.util.ArrayList;\n");
            code.append("import java.util.List;\n\n");
        }

        code.append("public class ").append(classModel.getName());

        if (!classModel.getParents().isEmpty()) {
            code.append(" extends ").append(classModel.getParents().get(0));
        }

        code.append(" {\n\n");

        for (ClassModel.Attribute attr : classModel.getAttributes()) {
            code.append("    private ").append(mapType(attr.getType()))
                    .append(" ").append(attr.getName()).append(";\n");
        }

        for (Relationship rel : classModel.getRelationships()) {
            String type = determineAttributeType(rel);
            code.append("    private ").append(type).append(" ")
                    .append(rel.getTargetClass().toLowerCase()).append(";\n");
        }

        code.append("\n");

        if (!classModel.getAttributes().isEmpty() || !classModel.getRelationships().isEmpty()) {
            code.append(generateConstructor(classModel));
        }

        for (ClassModel.Attribute attr : classModel.getAttributes()) {
            code.append(generateGetter(attr));
            code.append(generateSetter(attr));
        }

        for (ClassModel.Method method : classModel.getMethods()) {
            code.append(generateMethod(method));
        }

        code.append("}\n");

        return code.toString();
    }

    private String generateConstructor(ClassModel classModel) {
        StringBuilder code = new StringBuilder();
        code.append("    public ").append(classModel.getName()).append("(");

        List<ClassModel.Attribute> attrs = classModel.getAttributes();
        List<Relationship> constructorRels = getConstructorRelationships(classModel);
        
        int paramCount = 0;
        
        for (int i = 0; i < attrs.size(); i++) {
            ClassModel.Attribute attr = attrs.get(i);
            if (paramCount > 0) {
                code.append(", ");
            }
            code.append(mapType(attr.getType())).append(" ").append(attr.getName());
            paramCount++;
        }
        
        for (Relationship rel : constructorRels) {
            if (paramCount > 0) {
                code.append(", ");
            }
            String type = determineAttributeType(rel);
            String paramName = rel.getTargetClass().toLowerCase();
            code.append(type).append(" ").append(paramName);
            paramCount++;
        }

        code.append(") {\n");

        for (ClassModel.Attribute attr : attrs) {
            code.append("        this.").append(attr.getName())
                    .append(" = ").append(attr.getName()).append(";\n");
        }
        
        for (Relationship rel : constructorRels) {
            String paramName = rel.getTargetClass().toLowerCase();
            code.append("        this.").append(paramName)
                    .append(" = ").append(paramName).append(";\n");
        }

        code.append("    }\n\n");

        return code.toString();
    }
    
    private List<Relationship> getConstructorRelationships(ClassModel classModel) {
        List<Relationship> constructorRels = new java.util.ArrayList<>();
        
        for (Relationship rel : classModel.getRelationships()) {
            Relationship.RelationType type = rel.getType();
            String cardinality = rel.getCardinality();
            
            if (type == Relationship.RelationType.COMPOSITION) {
                if (!cardinality.contains("*")) {
                    constructorRels.add(rel);
                }
            }
            else if (type == Relationship.RelationType.ASSOCIATION_FORTE) {
                if (cardinality.startsWith("1") && !cardinality.contains("*")) {
                    constructorRels.add(rel);
                }
            }
        }
        
        return constructorRels;
    }

    private String generateGetter(ClassModel.Attribute attr) {
        String methodName = "get" + capitalize(attr.getName());
        return "    public " + mapType(attr.getType()) + " " + methodName + "() {\n" +
                "        return " + attr.getName() + ";\n" +
                "    }\n\n";
    }

    private String generateSetter(ClassModel.Attribute attr) {
        String methodName = "set" + capitalize(attr.getName());
        return "    public void " + methodName + "(" + mapType(attr.getType()) + " " + attr.getName() + ") {\n" +
                "        this." + attr.getName() + " = " + attr.getName() + ";\n" +
                "    }\n\n";
    }

    private String generateMethod(ClassModel.Method method) {
        StringBuilder code = new StringBuilder();

        code.append("    public ").append(mapType(method.getReturnType()))
                .append(" ").append(method.getName()).append("(");

        List<String[]> args = method.getArguments();
        for (int i = 0; i < args.size(); i++) {
            String[] arg = args.get(i);
            code.append(mapType(arg[1])).append(" ").append(arg[0]);
            if (i < args.size() - 1) {
                code.append(", ");
            }
        }

        code.append(") {\n");

        if (!method.getReturnType().equalsIgnoreCase("void")) {
            code.append("        return ").append(getDefaultValue(method.getReturnType())).append(";\n");
        }

        code.append("    }\n\n");

        return code.toString();
    }

    private String mapType(String type) {
        if (type == null) return "Object";
        
        if (type.startsWith("List<") && type.endsWith(">")) {
            String innerType = type.substring(5, type.length() - 1);
            return "List<" + mapType(innerType) + ">";
        }
        
        switch (type.toLowerCase()) {
            case "int": return "int";
            case "char": return "char";
            case "string": return "String";
            case "boolean": return "boolean";
            case "void": return "void";
            case "double": return "double";
            case "float": return "float";
            case "long": return "long";
            default: return type;
        }
    }

    private String getDefaultValue(String type) {
        if (type == null) return "null";
        
        if (type.startsWith("List<") || type.toLowerCase().startsWith("list<")) {
            return "new ArrayList<>()";
        }
        
        switch (type.toLowerCase()) {
            case "int": return "0";
            case "char": return "'\\0'";
            case "boolean": return "false";
            case "double": return "0.0";
            case "float": return "0.0f";
            case "long": return "0L";
            default: return "null";
        }
    }
    
    private boolean needsListImport(ClassModel classModel) {
        for (ClassModel.Attribute attr : classModel.getAttributes()) {
            if (attr.getType() != null && attr.getType().toLowerCase().startsWith("list<")) {
                return true;
            }
        }
        for (Relationship rel : classModel.getRelationships()) {
            if (rel.getCardinality() != null && rel.getCardinality().contains("*")) {
                return true;
            }
        }
        for (ClassModel.Method method : classModel.getMethods()) {
            if (method.getReturnType() != null && method.getReturnType().toLowerCase().startsWith("list<")) {
                return true;
            }
            for (String[] arg : method.getArguments()) {
                if (arg[1] != null && arg[1].toLowerCase().startsWith("list<")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String determineAttributeType(Relationship rel) {
        String cardinality = rel.getCardinality();

        if (cardinality.contains("*")) {
            return "List<" + rel.getTargetClass() + ">";
        } else {
            return rel.getTargetClass();
        }
    }
}
