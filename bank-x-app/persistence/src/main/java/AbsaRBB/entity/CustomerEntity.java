package AbsaRBB.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data  // Optional: if you're using Lombok
public class CustomerEntity {

    // If not using Lombok, add getters and setters:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    private String firstName;
    private String lastName;
    private String idNumber;
    private String emailAddress;

}