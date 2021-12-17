package wordy.interpreter;

public class WordyRuntimeTypeError extends RuntimeException {
    public WordyRuntimeTypeError(String message) {
        super(message);
    }
}
