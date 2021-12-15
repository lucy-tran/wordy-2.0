package wordy.ast.values;

public interface WordyValue {
    @Override
    boolean equals(Object obj);

    WordyType getType();
}
