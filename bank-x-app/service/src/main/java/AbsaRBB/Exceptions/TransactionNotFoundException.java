package AbsaRBB.Exceptions;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(String message){
        super (message);
    }

    public TransactionNotFoundException(Long id){
        super("Transaction: " + id + " Not Found");
    }
}
