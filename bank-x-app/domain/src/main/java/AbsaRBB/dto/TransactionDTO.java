package AbsaRBB.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private Long transactionID;
    private Long accountID;
    private Long externalBankID;
    private double transactionAmount;
    private String transactionType;
    private Date transactionDate;
    private Date createdDate;
    private boolean isExternal;
    private double transactionCharge;

}
