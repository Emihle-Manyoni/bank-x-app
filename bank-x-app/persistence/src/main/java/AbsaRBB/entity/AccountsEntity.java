package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    private String accountType;
    private int accountNumber;
    private double balance;
    private double creditedAmount;
    private double interestRate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

}