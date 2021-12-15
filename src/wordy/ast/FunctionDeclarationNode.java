package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;

/**
 * A variable reference (e.g. “x”) in a Wordy abstract syntax tree. Note that this is a variable
 * _usage_; Wordy does not have variable _declarations_.
 * 
 * This expression evaluates to the current value of the variable.
 */
public class FunctionDeclarationNode extends ExpressionNode {
    public enum ReturnType {
        VOID,
        CONSTANT
    }

    private final String name;
    private final ReturnType returnType;
    private VariableNode[] params;
    private BlockNode body;

    // TODO: Should functions have their own evaluation context?
    protected EvaluationContext functionContext;
    public Double returnValue;


    // TODO: Should the constructor include params or arguments?
    public FunctionDeclarationNode(String name, ReturnType returnType, BlockNode body, VariableNode... params) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
        this.functionContext = new EvaluationContext();
    }

    /**
     * The name of the function.
     */
    public String getName() {
        return name;
    }

    public ReturnType getReturnType() {
        return returnType;
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
        if (this.returnType != that.returnType) {
            return false;
        } else if (this.params != that.params) {
            return false;
        } else if (this.body != that.body) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "FunctionNode{name=" + name + ", returnType=" + returnType.toString() +
            ", params=" + params + ", body=" + body + '}';
    }

    @Override
    // TODO: Implement this method
    protected String describeAttributes() {
        return "(name=\"" + name + "\")";
    }

    @Override
    public void compile(PrintWriter out) {
        String type = returnType == ReturnType.VOID ? "void" : "double";
        StringBuilder paramsStr = new StringBuilder("(");
        for (int i = 0; i < params.length; i++) {
            if (i != params.length - 1) {
                paramsStr.append("double " + params[i].getName() + ", ");
            } else {
                paramsStr.append("double " + params[i].getName() + ")");
            }
        }
        out.print("public static " + type + " " + name + paramsStr);
        out.print(body);
    }

    // @Override
    // protected void doRun(EvaluationContext context) {
    // context.set(this.name, WordyValue);
    // // Where do we store the function declarations? Maybe WordyValue stored with all other
    // WordyValues
    // // => closures
    // // Function type of itself, not a wordyvalue => OOP
    // // can the function see things from the parent context => NO! this will lead to
    // for (VariableNode param : this.params) {
    // this.functionContext.set(param.getName(), 1);
    // // this.functionContext.set(param.getName(), context.get(argumentExpression));
    // }

    // while (true) {
    // try {
    // body.doRun(functionContext);
    // } catch (FunctionReturned exception) {
    // break;
    // }
    // }
    // }

    @Override
    protected double doEvaluate(EvaluationContext context) {
        // TODO Auto-generated method stub
        return 0;
    }
}
