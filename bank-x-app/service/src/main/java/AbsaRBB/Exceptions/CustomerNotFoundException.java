package AbsaRBB.Exceptions;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String message){
        super (message);
    }

    public CustomerNotFoundException(Long customerID){
        super("Customer: " + customerID + " Not Found");
    }
}
