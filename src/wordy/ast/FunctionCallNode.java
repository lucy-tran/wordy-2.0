package wordy.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import wordy.ast.values.WordyClosure;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;
import wordy.interpreter.WordyRuntimeTypeError;

/**
 * A variable reference (e.g. “x”) in a Wordy abstract syntax tree. Note that this is a variable
 * _usage_; Wordy does not have variable _declarations_.
 *
 * This expression evaluates to the current value of the variable.
 */
public class FunctionCallNode extends StatementNode {
    private final VariableNode name;
    private final List<ExpressionNode> arguments;
    public WordyValue returnValue;

    public FunctionCallNode(VariableNode name, List<ExpressionNode> arguments) {
        this.name = name;
        this.arguments = List.copyOf(arguments);
    }

    public FunctionCallNode(VariableNode name, ExpressionNode... arguments) {
        this.name = name;
        this.arguments = Arrays.asList(arguments);
    }

    /**
     * The name of the function.
     */
    public String getName() {
        return name.getName();
    }

    public WordyValue getReturnValue() {
        return returnValue;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> map = new TreeMap<String, ASTNode>();
        map.put("name", name);
        for (int i = 0; i < arguments.size(); i++) {
            map.put("arg " + (i + 1), arguments.get(i));
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FunctionCallNode that = (FunctionCallNode) o;
        return this.name.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "FunctionCallNode{name=" + name + ", args=" + arguments + '}';
    }

    @Override
    protected String describeAttributes() {
        String numOfArgs = "(%d %s)"
            .formatted(arguments.size(), arguments.size() == 1 ? "argument" : "arguments");
        return "(name=" + name + ", " + numOfArgs + ")";
    }

    @Override
    protected void doRun(EvaluationContext context) {
        WordyValue storedValue = context.get(name.getName());

        if (!(storedValue instanceof WordyClosure)) {
            throw new WordyRuntimeTypeError("Variable is of type " + storedValue.getType().toString() + ". Please make sure it is a closure.");
        }

        WordyClosure closure = ((WordyClosure) storedValue);
        StatementNode body = closure.getBody();
        List<String> params = closure.getParamNames();
        EvaluationContext functionContext = closure.context;

        try {
            for (int i = 0; i < params.size(); i++) {
                functionContext.set(params.get(i), arguments.get(i).doEvaluate(context));
            }
        } catch (IndexOutOfBoundsException exception) {
            throw new RuntimeException("The number of arguments must match the number of parameters!");
        }

        try {
            body.doRun(functionContext);
        } catch (FunctionReturned exception) {
            returnValue = functionContext.get("returnValue");

        }
    }
}
