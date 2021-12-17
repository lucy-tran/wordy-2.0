package wordy.ast;

import java.util.Map;
import java.util.Objects;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.WordyRuntimeTypeError;

import static wordy.ast.Utils.orderedMap;

/**
 * An assignment statement (“Set <variable> to <expression>”) in a Wordy abstract syntax tree.
 */
public class AssignmentNode extends StatementNode {
    /**
     * The left-hand side (LHS) of the assignment, the variable whose value will be updated.
     */
    private final VariableNode variable;

    /**
     * The right-hand side (RHS) of the assignment, the expression whose value will be assigned to the
     * LHS variable.
     */
    private final ASTNode rightHandNode;

    public AssignmentNode(VariableNode variable, ExpressionNode expression) {
        // Here, expression may be a FunctionDeclarationNode
        this.variable = variable;
        this.rightHandNode = expression;
    }

    public AssignmentNode(VariableNode variable, FunctionCallNode functionCallNode) {
        this.variable = variable;
        this.rightHandNode = functionCallNode;
    }


    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", variable,
            "rhs", rightHandNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AssignmentNode that = (AssignmentNode) o;
        return variable.equals(that.variable)
            && rightHandNode.equals(that.rightHandNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, rightHandNode);
    }

    @Override
    public String toString() {
        return "AssignmentStatement{"
            + "variable='" + variable + '\''
            + ", value=" + rightHandNode
            + '}';
    }

    @Override
    protected void doRun(EvaluationContext context) {
        if (rightHandNode instanceof ExpressionNode) {
            ExpressionNode expression = ((ExpressionNode) rightHandNode);
            context.set(variable.getName(), expression.doEvaluate(context));
        } else if (rightHandNode instanceof FunctionCallNode) {
            FunctionCallNode functionCallNode = ((FunctionCallNode) rightHandNode);
            functionCallNode.doRun(context);
            context.set(variable.getName(), functionCallNode.getReturnValue());
        } else {// This is just to make sure rhs is not a statement node other than FunctionCallNode.
            throw new WordyRuntimeTypeError("Right hand side must be an expression or function call.");
        }
    }
}
