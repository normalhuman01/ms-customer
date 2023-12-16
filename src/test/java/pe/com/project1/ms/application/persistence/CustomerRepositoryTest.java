package pe.com.project1.ms.application.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import pe.com.project1.ms.domain.Customer;
import pe.com.project1.ms.domain.CustomerType;
import pe.com.project1.ms.domain.DocumentType;
import pe.com.project1.ms.infrastructure.persistence.model.CustomerDao;
import pe.com.project1.ms.infrastructure.persistence.mongodb.ICustomerReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
class CustomerRepositoryTest {
    CustomerDao customerDao = new CustomerDao("456", "César", "Acuña", "Peralta", "45678912", DocumentType.DNI,
            "platacomocancha@gmail.com", LocalDate.of(1952, 8, 11), CustomerType.ENTERPRISE);
    @Autowired
    private CustomerRepository customerRepository;
    @MockBean
    private ICustomerReactiveMongoRepository customerReactiveMongoRepository;

    @BeforeEach
    public void setup() {
        customerDao = new CustomerDao("456", "César", "Acuña", "Peralta", "45678912", DocumentType.DNI,
                "platacomocancha@gmail.com", LocalDate.of(1952, 8, 11), CustomerType.ENTERPRISE);
    }

    @Test
    void testSave() {
        Customer customer = new Customer("456", "César", "Acuña", "Peralta", "45678912", DocumentType.DNI,
                "platacomocancha@gmail.com", LocalDate.of(1952, 8, 11), CustomerType.ENTERPRISE);
        when(customerReactiveMongoRepository.save(customerDao)).thenReturn(Mono.just(customerDao));

        Mono<Customer> monoCustomer = customerRepository.save(customer);

        StepVerifier.create(monoCustomer)
                .consumeNextWith(c -> {
                    assertInstanceOf(Customer.class, c);
                    assertEquals(customer.getId(), c.getId());
                }).verifyComplete();
    }

    @Test
    void testFindById() {
        when(customerReactiveMongoRepository.findById("456")).thenReturn(Mono.just(customerDao));

        StepVerifier.create(customerRepository.findById("456"))
                .consumeNextWith(found -> {
                    assertInstanceOf(Customer.class, found);
                    assertEquals("456", found.getId());
                }).verifyComplete();
    }

    @Test
    void testFindByDocumentNumber() {
        when(customerReactiveMongoRepository.findByDocumentNumber("45678912")).thenReturn(Mono.just(customerDao));

        StepVerifier.create(customerRepository.findByDocumentNumber("45678912"))
                .consumeNextWith(found -> {
                    assertInstanceOf(Customer.class, found);
                    assertEquals("45678912", found.getDocumentNumber());
                }).verifyComplete();
    }

    @Test
    void testFindAll() {
        when(customerReactiveMongoRepository.findAll()).thenReturn(Flux.just(new CustomerDao(), new CustomerDao()));

        StepVerifier.create(customerRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(customerReactiveMongoRepository.deleteById("456")).thenReturn(Mono.empty());

        StepVerifier.create(customerRepository.deleteById("456"))
                .expectNextCount(0)
                .verifyComplete();

        verify(customerReactiveMongoRepository, times(1)).deleteById("456");

    }

}
