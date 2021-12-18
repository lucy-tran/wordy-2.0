package wordy.ast.values;

import wordy.interpreter.EvaluationContext;

public class WordyRecord implements WordyValue {
    // private BlockNode body;
    // A WordyRecord value should not have a body, it should only store key-value pairs.
    private EvaluationContext context;

    public WordyRecord() {
        this.context = new EvaluationContext();
    }

    public EvaluationContext getContext() {
        return context;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WordyRecord) {
            WordyRecord that = ((WordyRecord) obj);
            return this.context.equals(that.getContext());
        } else {
            return false;
        }
    }

    @Override
    public WordyType getType() {
        return WordyType.Record;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("WordyRecord(");
        String prefix = "";
        for (var variableEntry : context.allVariables().entrySet()) {
            builder.append(prefix);
            prefix = ", ";
            builder.append(variableEntry.getKey());
            builder.append("=");
            builder.append(variableEntry.getValue());
        }
        builder.append(")");
        return builder.toString();
    }


}
