package wordy.ast;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import wordy.ast.values.WordyRecord;
import wordy.ast.values.WordyValue;
import wordy.interpreter.EvaluationContext;

// Set A to record of {a = 3,
// b = function of () in: ...,
// c = record of {d = 2}}.
// The whole thing is an AssignmentNode, lhs=VariableNode, rhs=RecordNode
//

public class RecordNode extends ExpressionNode {
    private final List<RecordRowNode> rows;

    public RecordNode(List<RecordRowNode> rows) {
        this.rows = List.copyOf(rows);
    }

    public RecordNode(RecordRowNode... rows) {
        this.rows = Arrays.asList(rows);
    }

    @Override
    protected WordyValue doEvaluate(EvaluationContext context) {
        WordyRecord record = new WordyRecord();
        EvaluationContext recordContext = record.getContext();
        for (RecordRowNode row : rows) {
            row.doRun(recordContext);
        }
        return record;
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        Map<String, ASTNode> result = new LinkedHashMap<>();
        var iter = rows.iterator();
        for (int index = 0; iter.hasNext(); index++) {
            result.put(String.valueOf(index), iter.next());
        }
        return result;
    }

    public List<RecordRowNode> getRows() {
        return rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RecordNode that = (RecordNode) o;
        return this.rows.equals(that.getRows());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }

    @Override
    public String toString() {
        return "RecordNode:" + describeAttributes();
    }

    @Override
    protected String describeAttributes() {
        return "(%d %s)"
            .formatted(rows.size(), rows.size() == 1 ? "child" : "children");
    }

}
