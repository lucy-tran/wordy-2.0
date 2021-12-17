package wordy.ast;

import java.util.Map;
import java.util.Objects;
import static wordy.ast.Utils.orderedMap;

import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;

public class FieldAssignmentNode extends StatementNode{
        /**
     * The left-hand side (LHS) of the assignment, the variable whose value will be updated.
     */
    private final FieldAccessNode variable;

    /**
     * The right-hand side (RHS) of the assignment, the expression whose value will be assigned to the
     * LHS variable.
     */
    private final BlockNode records;

    public FieldAssignmentNode (FieldAccessNode variable, BlockNode records) {
        this.variable = variable;
        this.records= records;
    }

    @Override
    protected void doRun(EvaluationContext context) {
        context.set(variable.getName(), variable.doEvaluate(context));
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", variable,
            "rhs", records);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FieldAssignmentNode that = (FieldAssignmentNode) o;
        return variable.equals(that.variable)
            && records.equals(that.records);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public String toString() {
        return "FieldAssignmentStatement{"
            + "Record='" + variable + '\''
            + ", Field=" + records
            + '}';
    }
    
}
