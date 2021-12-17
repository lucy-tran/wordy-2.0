package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import wordy.ast.values.WordyDouble;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;
import wordy.interpreter.WordyRuntimeTypeError;

import static wordy.ast.Utils.orderedMap;

/**
 * Two expressions joined by an operator (e.g. “x plus y”) in a Wordy abstract syntax tree.
 */
public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        EXPONENTIATION
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;

    public BinaryExpressionNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs);
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }

    @Override
    protected WordyDouble doEvaluate(EvaluationContext context) {
        WordyValue rightValue = rhs.doEvaluate(context);
        WordyValue leftValue = lhs.doEvaluate(context);

        if (rightValue == null) { // e.g. a variable doesn't exist yet
            rightValue = new WordyDouble(0);
        }
        if (leftValue == null) {
            leftValue = new WordyDouble(0);
        }

        if (!(rightValue instanceof WordyDouble) || !(leftValue instanceof WordyDouble)) {
            throw new WordyRuntimeTypeError("Cannot do mathematical operations on non-numeric types.");
        }

        double right = ((WordyDouble) rightValue).getValue();
        double left = ((WordyDouble) leftValue).getValue();

        switch (operator) {
            case ADDITION:
                return new WordyDouble(right + left);
            case SUBTRACTION:
                return new WordyDouble(left - right);
            case MULTIPLICATION:
                return new WordyDouble(right * left);
            case DIVISION:
                return new WordyDouble(left / right);
            case EXPONENTIATION:
                return new WordyDouble(Math.pow(left, right));
        }
        throw new UnsupportedOperationException("Unknown operator:" + operator);
    }
}
