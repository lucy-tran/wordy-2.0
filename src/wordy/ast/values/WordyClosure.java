package wordy.ast.values;

import java.util.ArrayList;
import java.util.List;

import wordy.ast.BlockNode;
import wordy.interpreter.EvaluationContext;

public class WordyClosure implements WordyValue {
    private BlockNode body;
    private final List<String> paramNames;
    public EvaluationContext context;

    public WordyClosure(List<String> paramNames, BlockNode body) {
        this.body = body;
        this.paramNames = paramNames;
        this.context = new EvaluationContext();
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public WordyType getType() {
        return WordyType.Closure;
    }
}
