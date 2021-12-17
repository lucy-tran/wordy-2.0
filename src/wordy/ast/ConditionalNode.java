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
 * A conditional (“If … then”) in a Wordy abstract syntax tree.
 * 
 * Wordy only supports direct comparisons between two numeric expressions, e.g. "If x is less than y
 * then….” Wordy does not support boolean operators, or arbitrary boolean expressions. The general
 * structure of a Wordy conditional is:
 * 
 * If <lhs> <operator> <rhs> then <ifTrue> else <ifFalse>
 */
public class ConditionalNode extends StatementNode {
    public enum Operator {
        EQUALS,
        LESS_THAN,
        GREATER_THAN
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;
    private final StatementNode ifTrue, ifFalse;

    public ConditionalNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs, StatementNode ifTrue, StatementNode ifFalse) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs,
            "ifTrue", ifTrue,
            "ifFalse", ifFalse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConditionalNode that = (ConditionalNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs)
            && this.ifTrue.equals(that.ifTrue)
            && this.ifFalse.equals(that.ifFalse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs, ifTrue, ifFalse);
    }

    @Override
    public String toString() {
        return "ConditionalNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + ", trueBlock=" + ifTrue
            + ", falseBlock=" + ifFalse
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }

    @Override
    protected void doRun(EvaluationContext context) {
        WordyValue lhs_value = lhs.doEvaluate(context);
        WordyValue rhs_value = rhs.doEvaluate(context);

        if (!(lhs_value instanceof WordyDouble) || !(rhs_value instanceof WordyDouble)) {
            throw new WordyRuntimeTypeError("Cannot do mathematical operations on non-numeric types.");
        }

        double left = ((WordyDouble) lhs_value).getValue();
        double right = ((WordyDouble) rhs_value).getValue();

        Boolean conditional_result = false;

        switch (operator) {
            case EQUALS:
                if (left == right)
                    conditional_result = true;
                break;
            case LESS_THAN:
                if (left < right)
                    conditional_result = true;
                break;
            case GREATER_THAN:
                if (left > right)
                    conditional_result = true;
                break;
        }

        if (conditional_result)
            ifTrue.doRun(context);
        else
            ifFalse.doRun(context);
    }

    @Override
    public void compile(PrintWriter out) {
        out.print("if(");
        switch (operator) {
            case EQUALS:
                lhs.compile(out);
                out.print("=");
                rhs.compile(out);
                break;
            case LESS_THAN:
                lhs.compile(out);
                out.print("<");
                rhs.compile(out);
                break;
            case GREATER_THAN:
                lhs.compile(out);
                out.print(">");
                rhs.compile(out);
                break;
        }
        out.print(")");
        ifTrue.compile(out);
        out.print("else ");
        ifFalse.compile(out);
    }
}
