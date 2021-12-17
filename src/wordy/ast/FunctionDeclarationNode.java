package wordy.ast;

import java.util.List;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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
    private List<VariableNode> params;
    private StatementNode body;

    public FunctionDeclarationNode(StatementNode body, List<VariableNode> params) {
        this.body = body;
        this.params = List.copyOf(params);
    }

    // TODO: Should the constructor include params or arguments?
    public FunctionDeclarationNode(StatementNode body, VariableNode... params) {
        this.params = Arrays.asList(params);
        this.body = body;
    }

    @Override
    // TODO: What are the children of a FunctionNode?
    public Map<String, ASTNode> getChildren() {
        return Map.of("body", body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionDeclarationNode that = (FunctionDeclarationNode) o;
        // if (this.returnType != that.returnType) {
        // return false;
        // } else
        if (this.params != that.params) {
            return false;
        } else if (this.body != that.body) {
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
        // return "FunctionDeclarationNode{returnType=" + returnType.toString() +
        // ", params=" + params + ", body=" + body + '}';
        return "FunctionDeclarationNode{params=" + params + ", body=" + body + '}';
    }

    @Override
    // TODO: Implement this method
    protected String describeAttributes() {
        StringBuilder paramsStr = new StringBuilder("[");
        for (int i = 0; i < params.size(); i++) {
            if (i != params.size() - 1) {
                paramsStr.append(params.get(i).describeAttributes() + ", ");
            } else {
                paramsStr.append(params.get(i).describeAttributes() + "]");
            }
        }
        // return "(returnType=" + returnType.toString() +
        // ", params=" + paramsStr + ", body=" + body.describeAttributes() + ')';
        return "(params=" + paramsStr + ", body=" + body.describeAttributes() + ')';
    }

    // @Override
    // public void compile(PrintWriter out) {
    // String type = returnType == ReturnType.WordyNull ? "void" : "double";
    // StringBuilder paramsStr = new StringBuilder("(");
    // for (int i = 0; i < params.length; i++) {
    // if (i != params.length - 1) {
    // paramsStr.append("double " + params[i].getName() + ", ");
    // } else {
    // paramsStr.append("double " + params[i].getName() + ")");
    // }
    // }
    // out.print("public static " + type + " " + name + paramsStr);
    // out.print(body);
    // }

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
