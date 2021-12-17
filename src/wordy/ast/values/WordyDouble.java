package wordy.ast.values;


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
}
