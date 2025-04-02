package AbsaRBB;

import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.entity.CustomerEntity;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private AccountsDomainRepo accountsDomainRepo;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountsRepository accountsRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void creatingACustomerTest() {
        //GIVEN
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("Andiswa");
        customer.setLastName("Mafuleka");
        customer.setEmailAddress("andiswamafuleka@gmail.com");
        customer.setIdNumber("773277397912");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerID(1L);
        customerEntity.setFirstName("Andiswa");
        customerEntity.setLastName("Mafuleka");
        customerEntity.setEmailAddress("andiswamafuleka@gmail.com");
        customerEntity.setIdNumber("773277397912");

        //WHEN
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        when(accountsDomainRepo.save(any(AccountsDTO.class))).thenReturn(new AccountsDTO());

        CustomerDTO createdCustomer = customerService.createCustomer(customer);

        //THEN
        Assertions.assertNotNull(createdCustomer.getCustomerID());
        Assertions.assertEquals("Andiswa", createdCustomer.getFirstName());
        Assertions.assertEquals("Mafuleka", createdCustomer.getLastName());
        Assertions.assertEquals("andiswamafuleka@gmail.com", createdCustomer.getEmailAddress());
        Assertions.assertEquals("773277397912", createdCustomer.getIdNumber());
    }

    @Test
    public void finding_Customer_By_ID_Test_From_Customer_Service(){
        //Given
        CustomerDTO customerDTO = new CustomerDTO();
          customerDTO.setCustomerID(1L);

        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerID(1L);

        //WHEN
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO foundCustomer = customerService.getCustomerById(1L);

        //THEN
        Assertions.assertNotNull(foundCustomer.getCustomerID());
    }

//    @Test
//    public void given_that_CustomerID_Is_Not_Found_Test(){
//
//    }
}
