package wordy.ast;

import java.util.Map;

import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;

public class NullNode extends ExpressionNode {

    @Override
    protected WordyValue doEvaluate(EvaluationContext context) {
        return null;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
