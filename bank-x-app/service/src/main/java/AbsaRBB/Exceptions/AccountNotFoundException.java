package AbsaRBB.Exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message){
        super (message);
    }

    public AccountNotFoundException(Long id){
        super("Account: " + id + " Not Found");
    }
}
