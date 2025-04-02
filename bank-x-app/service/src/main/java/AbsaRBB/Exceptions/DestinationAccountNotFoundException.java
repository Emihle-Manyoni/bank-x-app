package AbsaRBB.Exceptions;

public class DestinationAccountNotFoundException extends RuntimeException{
    public DestinationAccountNotFoundException(String message){
        super (message);
    }

    public DestinationAccountNotFoundException(Long id){
        super("Destination Account: " + id + " Not Found");
    }
}