package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "Reconciliation")
public class ReconciliationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReconciliationID")
    private Long reconciliationID;
    @Column(name = "TransactionAmount")
    private double transactionAmount;
    @Column(name = "TransactionType")
    private String transactionType;
    @Column(name = "Status")
    private String status;
    @Column(name = "ReconciliationStatus")
    private String reconciliationStatus;
    @Column(name = "BankName")
    private String bankName;
    @Column(name = "BankCode")
    private String bankCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TransactionDate")
    private java.util.Date transactionDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private java.util.Date createdDate;
    @Column(name = "TransactionCharge")
    private double transactionCharge;
}
