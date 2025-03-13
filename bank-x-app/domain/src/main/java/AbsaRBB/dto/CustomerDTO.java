package AbsaRBB.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerDTO {
    private Long customerID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String idNumber;
    private List<AccountsDTO> accounts = new ArrayList<>();
}
