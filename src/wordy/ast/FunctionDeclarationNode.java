package wordy.ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import wordy.ast.values.WordyClosure;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;

/**
 * A variable reference (e.g. “x”) in a Wordy abstract syntax tree. Note that this is a variable
 * _usage_; Wordy does not have variable _declarations_.
 * 
 * This expression evaluates to the current value of the variable.
 */
public class FunctionDeclarationNode extends ExpressionNode {
    private final List<VariableNode> params;
    private final StatementNode body;

    public FunctionDeclarationNode(StatementNode body, List<VariableNode> params) {
        this.body = body;
        this.params = List.copyOf(params);
    }

    public FunctionDeclarationNode(StatementNode body, VariableNode... params) {
        this.body = body;
        this.params = Arrays.asList(params);
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> map = new TreeMap<String, ASTNode>();
        for (int i = 0; i < params.size(); i++) {
            map.put("param " + (i + 1), params.get(i));
        }
        map.put("body", body);
        return Map.of("body", body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionDeclarationNode that = (FunctionDeclarationNode) o;
        if (!this.params.equals(that.params)) {
            return false;
        } else if (!this.body.equals(that.body)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

    @Override
    public String toString() {
        return "FunctionDeclarationNode{params=" + params + ", body=" + body + '}';
    }

    @Override
    protected String describeAttributes() {
        StringBuilder paramsStr = new StringBuilder("[");
        for (int i = 0; i < params.size(); i++) {
            if (i != params.size() - 1) {
                paramsStr.append(params.get(i).describeAttributes() + ", ");
            } else {
                paramsStr.append(params.get(i).describeAttributes() + "]");
            }
        }
        return "(params=" + paramsStr + ", body=" + body.describeAttributes() + ')';
    }

    @Override
    protected WordyValue doEvaluate(EvaluationContext context) {
        List<String> paramNames = new ArrayList<>();
        for (VariableNode param : params) {
            paramNames.add(param.getName());
        }
        WordyClosure closure = new WordyClosure(paramNames, body);
        return closure;
    }
}
