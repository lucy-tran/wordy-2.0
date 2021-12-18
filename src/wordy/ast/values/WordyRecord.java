package wordy.ast.values;

import java.util.ArrayList;
import java.util.List;

import wordy.ast.AssignmentNode;
import wordy.ast.BlockNode;
import wordy.ast.StatementNode;
import wordy.interpreter.EvaluationContext;

public class WordyRecord implements WordyValue{

    private BlockNode body;
    // private final List<String> paramNames;
    public EvaluationContext context;

    public WordyRecord(BlockNode body) {
        this.body = body;
        this.context = new EvaluationContext();
    }

    public StatementNode getBody() {
        return body;
    }

    @Override
    public WordyType getType() {
        return WordyType.Record;
    }
    
    @Override
    public String toString() {
        String result = "WordyRecord(";
        List<StatementNode> statements = body.getStatements();
        for (StatementNode statement : statements){
            if (statement instanceof AssignmentNode){
                AssignmentNode assign = (AssignmentNode) statement;
                result = result + assign.toString();

            }
            result = result + statement.toString();
        }

        return result+")"; //"WordyRecord( "+ statements.toString()+")";
    }

    
}
