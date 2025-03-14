package AbsaRBB.entity;

import jakarta.persistence.*;
import  lombok.*;


@Entity
@Getter
@Setter
@Table(name = "ExternalBank")
public class ExternalBankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExternalBankID")
    private Long externalBankID;
    @Column(name = "BankName")
    private String bankName;
    @Column(name = "BankCode")
    private String bankCode;
}
