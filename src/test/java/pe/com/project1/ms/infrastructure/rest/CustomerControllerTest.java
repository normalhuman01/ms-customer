package pe.com.project1.ms.infrastructure.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.project1.ms.application.CustomerService;
import pe.com.project1.ms.domain.Customer;
import pe.com.project1.ms.domain.CustomerType;
import pe.com.project1.ms.domain.DocumentType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {CustomerHandler.class, CustomerRouter.class})
@ActiveProfiles("local")
class  CustomerControllerTest {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private CustomerService customerService;
	
	private static final Map<String, Customer> customers = new HashMap<>();
	
	@BeforeAll
	public static void setup() {
		customers.put("123", new Customer("123", "Luis Alberto", "Suárez", "Díaz", "12345678", DocumentType.DNI,
				"lasuarez@gmail.com", LocalDate.of(1987, 1, 24), CustomerType.PERSONAL));
		customers.put("321", new Customer("321", "Gianluca", "Lapadula", "Vargas", "87654321", DocumentType.DNI,
				"gianluca@gmail.com", LocalDate.of(1990, 2, 07), CustomerType.PERSONAL_VIP));
		customers.put("456", new Customer("456", "César", "Acuña", "Peralta", "45678912", DocumentType.DNI,
				"platacomocancha@gmail.com", LocalDate.of(1952, 8, 11), CustomerType.ENTERPRISE));
	}
	
	@Test
	void testCreate() {
		final Customer customer = customers.get("123");
		final Mono<Customer> customerMono = Mono.just(customer);
		
		when(customerService.save(customer)).thenReturn(customerMono);
		
		webTestClient.post().uri("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.body(customerMono, Customer.class)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Customer.class)
				.value(Assertions::assertNotNull)
				.value(customerCreated -> {
					assertEquals("123", customerCreated.getId());
					assertEquals("Luis Alberto", customerCreated.getNames());
					assertEquals("Suárez", customerCreated.getFirstSurname());
					assertEquals("Díaz", customerCreated.getLastSurname());
					assertEquals("12345678", customerCreated.getDocumentNumber());
					assertEquals(DocumentType.DNI, customerCreated.getDocumentType());
					assertEquals("lasuarez@gmail.com", customerCreated.getEmail());
					assertEquals(LocalDate.of(1987, 1, 24), customerCreated.getBirthdate());
					assertEquals(CustomerType.PERSONAL, customerCreated.getCustomerType());
				});
		verify(customerService, times(1)).save(customer);
	}

	@Test
	void testFindById() {
		final String customerId = "321";
		final Customer customer = customers.get(customerId);

		when(customerService.findById(customerId)).thenReturn(Mono.just(customer));
		
		webTestClient.get().uri("/customers/{id}", customerId)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Customer.class)
			.value(Assertions::assertNotNull)
			.value(customerFound -> {
				assertEquals("321", customerFound.getId());
				assertEquals("Gianluca", customerFound.getNames());
				assertEquals("Lapadula", customerFound.getFirstSurname());
				assertEquals("Vargas", customerFound.getLastSurname());
				assertEquals("87654321", customerFound.getDocumentNumber());
				assertEquals(DocumentType.DNI, customerFound.getDocumentType());
				assertEquals("gianluca@gmail.com", customerFound.getEmail());
				assertEquals(LocalDate.of(1990, 2, 07), customerFound.getBirthdate());
				assertEquals(CustomerType.PERSONAL_VIP, customerFound.getCustomerType());
			});
		verify(customerService, times(1)).findById(customerId);
	}

	@Test
	void testFindAll() {
		final Flux<Customer> customerFlux = Flux.fromStream(customers.values().stream());
				
		when(customerService.findAll()).thenReturn(customerFlux);
		
		webTestClient.get().uri("/customers")
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Customer.class)
			.value(Assertions::assertNotNull)
			.value(c -> assertEquals(3, c.size()));
		verify(customerService, times(1)).findAll();
	}

	@Test
	void testUpdate() {
		final String customerId = "456";
		final Customer customer = customers.get(customerId);
		final Mono<Customer> monoCustomer = Mono.just(customer);
	    
	    when(customerService.update(customer, customerId)).thenReturn(monoCustomer);

        webTestClient.put().uri("/customers/{id}", customerId)
                .body(monoCustomer, Customer.class)
                .exchange()
                .expectStatus().isOk();
        verify(customerService, times(1)).update(customer, customerId);
	}

	@Test
	void testDeleteById() {
		final String customerId = "123";

		when(customerService.deleteById(customerId)).thenReturn(Mono.empty());
		
		webTestClient.delete().uri("/customers/{id}", customerId)
        		.exchange()
        		.expectStatus().isNoContent();
        verify(customerService, times(1)).deleteById(customerId);
	}

}
