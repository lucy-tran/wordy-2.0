package wordy.ast.values;

import java.util.Map;

import wordy.interpreter.EvaluationContext;

public class WordyDouble implements WordyValue {
    private final double value;

    public WordyDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static int compare(WordyDouble first, WordyDouble second) {
        assert first != null;
        assert second != null;
        return Double.compare(first.getValue(), second.getValue());
    }

    public String toString() {
        return "WordyDouble(" + value + ")";
    }

    @Override
    public WordyType getType() {
        return WordyType.Double;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof WordyDouble){
            WordyDouble a = (WordyDouble) obj;
            return value==a.getValue();
        }
        else{
            return false;
        }
    }
}
