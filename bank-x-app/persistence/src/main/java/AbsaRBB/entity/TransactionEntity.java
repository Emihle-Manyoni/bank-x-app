package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "Transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID")
    private Long transactionID;

    @ManyToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private AccountsEntity account;

    @ManyToOne
    @JoinColumn(name = "ExternalBankID")
    private ExternalBankEntity externalBank;
    @Column(name = "TransactionAmount")
    private double transactionAmount;
    @Column(name = "TransactionType")
    private String transactionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TransactionDate")
    private java.util.Date transactionDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private java.util.Date createdDate;
    @Column(name = "isExternal")
    private boolean isExternal;
    @Column(name = "TransactionCharge")
    private double transactionCharge;
}
