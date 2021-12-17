package wordy.ast;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import wordy.ast.values.WordyDouble;
import wordy.interpreter.EvaluationContext;

/**
 * A literal floating-point value (e.g. “3.141”) in a Wordy abstract syntax tree.
 */
public final class ConstantNode extends ExpressionNode {
    private final WordyDouble wordyDouble;

    public ConstantNode(double value) {
        this.wordyDouble = new WordyDouble(value);
    }

    public ConstantNode(WordyDouble value) {
        this.wordyDouble = value;
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
        ConstantNode other = (ConstantNode) o;
        return WordyDouble.compare(this.wordyDouble, other.wordyDouble) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordyDouble);
    }

    @Override
    public String toString() {
        return "ConstantNode{value=" + wordyDouble.getValue() + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(value=" + wordyDouble.getValue() + ')';
    }

    @Override
    protected WordyDouble doEvaluate(EvaluationContext context) {
        return wordyDouble;
    }
}
