package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    @ManyToOne
    @JoinColumn(name = "accountID", nullable = false)
    private AccountsEntity account;

    @ManyToOne
    @JoinColumn(name = "externalBankID")
    private ExternalBankEntity externalBank;

    private int transactionAmount;
    private String transactionType;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date transactionDate;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdDate;

    private boolean isExternal;
    private double transactionCharge;
}
