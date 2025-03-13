package AbsaRBB.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDTO {
    private Long accountID;
    private Long customerID;
    private String accountType;
    private long accountNumber;
    private double creditedAmount;
    private double balance;
    private double interestRate;

}
