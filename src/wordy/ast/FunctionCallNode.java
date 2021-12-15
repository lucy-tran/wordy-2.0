package wordy.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wordy.ast.values.WordyClosure;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;

/**
 * A variable reference (e.g. “x”) in a Wordy abstract syntax tree. Note that this is a variable
 * _usage_; Wordy does not have variable _declarations_.
 *
 * This expression evaluates to the current value of the variable.
 */
public class FunctionCallNode extends StatementNode {
    private final String name;
    private List<ExpressionNode> arguments;
    public WordyValue returnValue;

    // TODO: Should the constructor include params or arguments?
    public FunctionCallNode(String name, ExpressionNode... arguments) {
        this.name = name;
        this.arguments = Arrays.asList(arguments);
    }

    /**
     * The name of the function.
     */
    public String getName() {
        return name;
    }

    public WordyValue getReturnValue() {
        return returnValue;
    }

    @Override
    // TODO: What are the children of a FunctionCallNode?
    public Map<String, ASTNode> getChildren() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionCallNode that = (FunctionCallNode) o;
        return this.name.equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "FunctionCallNode{name=" + name + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(name=\"" + name + "\")";
    }

    @Override
    protected void doRun(EvaluationContext context) {
        // TODO: Handle the case where context.get(this.name) is not a closure.
        WordyClosure closure = ((WordyClosure) context.get(this.name));
        BlockNode body = closure.getBody();
        List<String> params = closure.getParamNames();
        EvaluationContext functionContext = closure.context;
        // Where do we store the function declarations? Maybe WordyValue stored with all other
        // WordyValues
        // => closures
        // Function type of itself, not a wordyvalue => OOP
        // can the function see things from the parent context => NO! this will lead to

        try {
            for (int i = 0; i < params.size(); i++) {
                functionContext.set(params.get(i), arguments.get(i).doEvaluate(context));
            }
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("The number of arguments must match the number of parameters!");
        }

        while (true) {
            try {
                body.doRun(functionContext);
            } catch (FunctionReturned exception) {
                returnValue = functionContext.get("returnValue");
                break;
            }
        }
    }
}
