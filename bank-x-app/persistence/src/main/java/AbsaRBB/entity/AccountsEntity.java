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
@Table(name = "Accounts")
public class AccountsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Long accountID;
    @Column(name = "AccountType")
    @Enumerated(EnumType.STRING)
    private String accountType;
    @Column(name = "AccountNumber")
    private long accountNumber;
    @Column(name = "Balance")
    private double balance;
    @Column(name = "CreditedAmount")
    private double creditedAmount;
    @Column(name = "InterestRate")
    private double interestRate;

    //Stop the buss where are we going
    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private CustomerEntity customer;

}