package pe.com.project1.ms.infrastructure.persistence.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;
import pe.com.project1.ms.domain.CustomerType;
import pe.com.project1.ms.domain.DocumentType;
import pe.com.project1.ms.infrastructure.persistence.model.CustomerDao;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@Slf4j
@ActiveProfiles("local")
class CustomerReactiveMongoRepositoryTest {

	@Autowired
	private ICustomerReactiveMongoRepository iCustomerReactiveMongoRepository;
	private static final List<CustomerDao> customers = new ArrayList<>();
	private Mono<Void> deleteAllMono;

	@BeforeAll
	public static void init() {
		customers.add(new CustomerDao("Luis Alberto", "Suárez", "Díaz", "12345678", DocumentType.DNI,
				"lasuarez@gmail.com", LocalDate.of(1987, 1, 24), CustomerType.PERSONAL));
		customers.add(new CustomerDao("Gianluca", "Lapadula", "Vargas", "87654321", DocumentType.DNI,
				"gianluca@gmail.com", LocalDate.of(1990, 2, 07), CustomerType.PERSONAL_VIP));
		customers.add(new CustomerDao("César", "Acuña", "Peralta", "45678912", DocumentType.DNI,
				"platacomocancha@gmail.com", LocalDate.of(1952, 8, 11), CustomerType.ENTERPRISE));
	}

	@BeforeEach
	public void setup() {
		deleteAllMono = iCustomerReactiveMongoRepository.deleteAll();
	}

	@Test
	void testFindByDocumentNumber() {
		final String documentNumber = "87654321";
		final Mono<CustomerDao> customerMono = deleteAllMono
				.then(iCustomerReactiveMongoRepository.save(customers.get(1)))
				.then(iCustomerReactiveMongoRepository.findByDocumentNumber(documentNumber));
		
		StepVerifier.create(customerMono)
			.consumeNextWith(c -> {
				assertEquals(documentNumber, c.getDocumentNumber());
			}).verifyComplete();
	}

	@Test
	void testSave() {
		final CustomerDao customerDao = customers.get(0);
		final String documentNumber = "12345678";
		final Mono<CustomerDao> customerMono = deleteAllMono.then(iCustomerReactiveMongoRepository.save(customerDao))
				.then(iCustomerReactiveMongoRepository.findByDocumentNumber(documentNumber));
		StepVerifier.create(customerMono).consumeNextWith(customer -> {
			assertNotNull(customer.getId());
			assertEquals("Luis Alberto", customer.getNames());
			assertEquals("Suárez", customer.getFirstSurname());
			assertEquals("Díaz",customer.getLastSurname());
			assertEquals("12345678", customer.getDocumentNumber());
			assertEquals(DocumentType.DNI, customer.getDocumentType());
			assertEquals("lasuarez@gmail.com", customer.getEmail());
			assertEquals(LocalDate.of(1987, 1, 24), customer.getBirthdate());
			assertEquals(CustomerType.PERSONAL, customer.getCustomerType());
		}).verifyComplete();
	}

}
