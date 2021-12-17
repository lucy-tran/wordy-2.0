package wordy.ast;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

import wordy.interpreter.EvaluationContext;
import wordy.interpreter.FunctionReturned;

/**
 * A statement that causes program flow to exit the nearest-nested loop. Often called “break” in
 * other languages.
 * 
 * The interpreter implements this by throwing a `LoopExited` exception.
 */
public final class FunctionReturnNode extends StatementNode {
    private ASTNode returnNode;

    public FunctionReturnNode(ExpressionNode returnNode) {
        this.returnNode = returnNode;
    }

    public FunctionReturnNode(FunctionCallNode functionCallNode) {
        this.returnNode = functionCallNode;
    }

    public FunctionReturnNode() {
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        if (returnNode == null) {
            return Collections.emptyMap();
        }
        return Map.of("returnNode", returnNode);
    }

    @Override
    public boolean equals(Object o) {
        return this == o
            || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        if (returnNode == null) {
            return "FunctionReturnNode";
        }
        return "FunctionReturnNode{returnNode=" + returnNode.toString() + "}";
    }

    @Override
    public void doRun(EvaluationContext context) {
        if (returnNode instanceof ExpressionNode) {
            ExpressionNode returnExpression = ((ExpressionNode) returnNode);
            context.set("returnValue", returnExpression.doEvaluate(context));
        } else if (returnNode instanceof FunctionCallNode) {
            FunctionCallNode functionCallNode = ((FunctionCallNode) returnNode);
            functionCallNode.doRun(context);
            context.set("returnValue", functionCallNode.getReturnValue());
        }
        throw new FunctionReturned();
    }

    @Override
    public void compile(PrintWriter out) {
        out.print("return ");
        returnNode.compile(out);
        out.print("; ");
    }
}
