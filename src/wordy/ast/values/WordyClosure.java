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

    @Override
    public String toString() {
        if (body instanceof BlockNode) {
            BlockNode blockNode = ((BlockNode) body);
            return "WordyClosure(params=" + paramNames.toString() + ", body=" + blockNode.describeAttributes() + ")";
        } else {
            return "WordyClosure(params=" + paramNames.toString() + ", body=" + body.toString() + ")";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WordyClosure) {
            WordyClosure that = (WordyClosure) obj;
            if (!this.paramNames.equals(that.getParamNames())) {
                System.out.println("params not equal");
            }

            if (!this.body.equals(that.getBody())) {
                System.out.println("body not equal");
            }
            return this.paramNames.equals(that.getParamNames()) && this.body.equals(that.getBody());
        } else {
            return false;
        }
    }
}
