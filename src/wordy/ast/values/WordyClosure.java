package wordy.ast.values;

import java.util.List;

import wordy.ast.BlockNode;
import wordy.ast.StatementNode;
import wordy.interpreter.EvaluationContext;

public class WordyClosure implements WordyValue {
    private StatementNode body;
    private final List<String> paramNames;
    public EvaluationContext context;

    public WordyClosure(List<String> paramNames, StatementNode body) {
        this.body = body;
        this.paramNames = paramNames;
        this.context = new EvaluationContext();
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public StatementNode getBody() {
        return body;
    }

    @Override
    public WordyType getType() {
        return WordyType.Closure;
    }
}
