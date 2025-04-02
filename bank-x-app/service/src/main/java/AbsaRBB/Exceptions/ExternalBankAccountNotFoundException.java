package AbsaRBB.Exceptions;

public class ExternalBankAccountNotFoundException extends RuntimeException {
    public ExternalBankAccountNotFoundException(String message) {
        super(message);
    }

    public ExternalBankAccountNotFoundException(Long id) {
        super("External Bank Account: " + id + " Not Found");
    }
}
