package wordy.ast;

import java.util.Map;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import wordy.ast.values.WordyDouble;
import wordy.ast.values.WordyValue;
import wordy.demo.Playground;
import wordy.interpreter.EvaluationContext;

public class RecordNode extends ExpressionNode{
    private final String RecordName;
    // private final String FieldName;

    public RecordNode(String RecordName){
        // this.FieldName = FieldName;
        this.RecordName = RecordName;
    }

    @Override
    protected WordyValue doEvaluate(EvaluationContext context) {

        return context.get(RecordName);
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
        RecordNode that = (RecordNode) o;
        return this.RecordName.equals(that.RecordName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(RecordName);
    }

    @Override
    public String toString() {
        return "RecordNode:" + describeAttributes();
    }

    @Override
    protected String describeAttributes() {
        return "(RecordName=\"" + RecordName + "\" )"; //",FieldName =" + FieldName
    }

    public String getName() {
        return RecordName;
    }

    
}