package pe.com.project1.ms.infrastructure.persistence.mongodb;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import pe.com.project1.ms.application.persistence.CustomerRepository;
import pe.com.project1.ms.domain.Customer;
import pe.com.project1.ms.infrastructure.persistence.model.CustomerDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CustomerReactiveMongoRepository implements CustomerRepository {

	private final ICustomerReactiveMongoRepository iCustomerReactiveMongoRepository;
	
	@Override
	public Mono<Customer> save(Customer customer) {
		return iCustomerReactiveMongoRepository
				.save(new CustomerDao(customer))
				.map(CustomerDao::toCustomer);
	}

	@Override
	public Mono<Customer> findById(String id) {
		return iCustomerReactiveMongoRepository
				.findById(id)
				.map(CustomerDao::toCustomer);
	}

	@Override
	public Mono<Customer> findByDocumentNumber(String documentNumber) {
		return iCustomerReactiveMongoRepository
				.findByDocumentNumber(documentNumber)
				.map(CustomerDao::toCustomer);
	}

	@Override
	public Flux<Customer> findAll() {
		return iCustomerReactiveMongoRepository
				.findAll()
				.map(CustomerDao::toCustomer);
	}

	@Override
	public Mono<Void> deleteById(String id) {
		return iCustomerReactiveMongoRepository.deleteById(id);
	}

}
