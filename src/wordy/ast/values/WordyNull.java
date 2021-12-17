package wordy.ast.values;

public class WordyNull implements WordyValue {
    public WordyNull() {
    }

    @Override
    public WordyType getType() {
        return WordyType.Null;
    }
}
