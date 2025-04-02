package AbsaRBB.Exceptions;

public class SourceAccountNotFoundException extends RuntimeException {
    public SourceAccountNotFoundException(String message) {
        super(message);
    }

    public SourceAccountNotFoundException(Long id) {
        super("Source Account: " + id + " Not Found");
    }
}