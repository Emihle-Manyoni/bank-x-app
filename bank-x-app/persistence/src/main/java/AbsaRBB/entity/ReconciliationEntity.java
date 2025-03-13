package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class ReconciliationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reconciliationID;

    private double transactionAmount;
    private String transactionType;
    private String status;
    private String reconciliationStatus;
    private String bankName;
    private String bankCode;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date transactionDate;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdDate;

    private double transactionCharge;
}
