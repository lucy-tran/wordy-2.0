package wordy.ast;

import java.util.Map;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Objects;

import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;

public class FieldAccessNode extends ExpressionNode{
    private final String fieldName;
    // private final String objName;

    public FieldAccessNode(String fieldName){
        this.fieldName = fieldName;
        // this.objName = objName;
    }

    @Override
    protected WordyValue doEvaluate(EvaluationContext context) {
        return context.get(fieldName);
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return Collections.emptyMap();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FieldAccessNode that = (FieldAccessNode) o;
        return this.fieldName.equals(that.fieldName); // & this.objName.equals(that.objName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }

    @Override
    public String toString() {
        return "RecordNode:" + describeAttributes();
    }

    @Override
    protected String describeAttributes() {
        return "(RecordName=\"" + fieldName + "\")";
    }

    
}
