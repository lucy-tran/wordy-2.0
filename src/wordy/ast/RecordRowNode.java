package wordy.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static wordy.ast.Utils.orderedMap;

import wordy.ast.values.WordyClosure;
import wordy.ast.values.WordyRecord;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;
import wordy.interpreter.WordyRuntimeTypeError;

public class RecordRowNode extends StatementNode {
    /**
     * The left-hand side (LHS) of the assignment, the variable whose value will be updated.
     */
    private final VariableNode variable;

    /**
     * The right-hand side (RHS) of the assignment, the expression whose value will be assigned to the
     * LHS variable.
     */
    private final ASTNode value;

    public RecordRowNode(VariableNode variable, ExpressionNode value) {
        this.variable = variable;
        this.value = value;
    }

    public RecordRowNode(VariableNode variable, FunctionCallNode value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    protected void doRun(EvaluationContext context) {
        if (value instanceof ExpressionNode) {
            ExpressionNode expression = ((ExpressionNode) value);
            context.set(variable.getName(), expression.doEvaluate(context));
        } else if (value instanceof FunctionCallNode) {
            FunctionCallNode functionCallNode = ((FunctionCallNode) value);
            functionCallNode.doRun(context);
            context.set(variable.getName(), functionCallNode.getReturnValue());
        } else {// This is just to make sure the value is not a statement node other than FunctionCallNode.
            throw new WordyRuntimeTypeError("Right hand side must be an expression or function call.");
        }
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", variable,
            "rhs", value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RecordRowNode that = (RecordRowNode) o;
        return variable.equals(that.variable)
            && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, value);
    }


    @Override
    public String toString() {
        return "RecordRowNode{"
            + "name='" + variable + '\''
            + ", value=" + value
            + '}';
    }

}
