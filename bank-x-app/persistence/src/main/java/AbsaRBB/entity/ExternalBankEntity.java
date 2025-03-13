package AbsaRBB.entity;

import jakarta.persistence.*;
import  lombok.*;


@Entity
@Getter
@Setter
public class ExternalBankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long externalBankID;

    private String bankName;
    private int bankCode;
}
