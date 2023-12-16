package pe.com.project1.ms.application.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import pe.com.project1.ms.application.CustomerService;
import pe.com.project1.ms.domain.Customer;
import pe.com.project1.ms.domain.CustomerType;
import pe.com.project1.ms.domain.DocumentType;
import pe.com.project1.ms.infrastructure.persistence.mongodb.CustomerReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("local")
class CustomerServiceImplTest {

	@MockBean
	private CustomerReactiveMongoRepository customerRepository;
	@Autowired
	private CustomerService customerServiceImpl;

	private Customer customer;

	@BeforeEach
	public void setup() {
		customer = new Customer("321", "Gianluca", "Lapadula", "Vargas", "87654321", DocumentType.DNI,
				"gianluca@gmail.com", LocalDate.of(1990, 2, 07), CustomerType.PERSONAL_VIP);
	}

	@Test
	void testSaveCustomer() {
		when(customerRepository.findByDocumentNumber("87654321")).thenReturn(Mono.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(new Customer()));

		StepVerifier.create(customerServiceImpl.save(customer))
			.expectNextCount(1)
			.verifyComplete();
	}

	@Test
	void testSaveCustomerWithExistingDocumentNumber() {

		when(customerRepository.findByDocumentNumber("87654321")).thenReturn(Mono.just(customer));
		when(customerRepository.save(any(Customer.class))).thenReturn(
				Mono.error(new RuntimeException("Este numero de documento ya le pertenece a otra persona")));

		Mono<Customer> customerMono = customerServiceImpl.save(customer);
		StepVerifier.create(customerMono)
				.expectErrorMatches(throwable -> throwable instanceof RuntimeException
						&& throwable.getMessage().equals("Este numero de documento ya le pertenece a otra persona"))
				.verify();
		//verify(customerRepository, times(0)).save(any(Customer.class));
	}

	@Test
	void testUpdateCustomer() {

		Customer customer2 = new Customer("321", "Gianluca", "Lapadula", "Vargas", "87654321", DocumentType.DNI,
				"gianluca@gmail.com", LocalDate.of(1990, 2, 07), CustomerType.PERSONAL_VIP);

		when(customerRepository.findById("321")).thenReturn(Mono.just(customer));
		when(customerRepository.save(customer2)).thenReturn(Mono.just(customer2));

		Mono<Customer> customerMono = customerServiceImpl.update(customer2, "321");

		StepVerifier.create(customerMono)
			.expectNextCount(1)
			.verifyComplete();
		
        verify(customerRepository, times(1)).save(any(Customer.class));

	}

}
