package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * An assignment statement (“Set <variable> to <expression>”) in a Wordy abstract syntax tree.
 */
public class RecordNode extends StatementNode {
    public static final RecordNode EMPTY = new RecordNode();

    private final List<AssignmentNode> assignments;

    public RecordNode(List<AssignmentNode> assignments) {
        this.assignments= List.copyOf(assignments);
    }

    public RecordNode(AssignmentNode... assignments) {
        this.assignments= Arrays.asList(assignments);
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> result = new LinkedHashMap<>();
        var iter = assignments.iterator();
        for(int index = 0; iter.hasNext(); index++) {
            result.put(String.valueOf(index), iter.next());
        }
        return result;
    }

    // @Override
    // public boolean equals(Object o) {
    //     if(this == o)
    //         return true;
    //     if(o == null || getClass() != o.getClass())
    //         return false;
    //     BlockNode blockNode = (BlockNode) o;
    //     return assignments.equals(RecordNode.assignments);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(assignments);
    }

    @Override
    public String toString() {
        return "RecordNode{statements=" + assignments + '}';
    }

    // @Override
    // protected String describeAttributes() {
    //     return "(%d %s)"
    //         .formatted(assignments.size(), statements.size() == 1 ? "child" : "children");
    // }

    @Override
    protected void doRun(EvaluationContext context) {
        for (AssignmentNode assignment : assignments){
            assignment.doRun(context);
        }
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return false;
    }

    // @Override
    // public void compile(PrintWriter out) {
    //     out.println("{");
    //     for (AssignmentNode assignment : assignments){
    //         statement.compile(out);
    //     }
    //     out.println("}");
    // }
}
