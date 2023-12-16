package pe.com.project1.ms.application.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.project1.ms.application.CustomerService;
import pe.com.project1.ms.application.persistence.CustomerRepository;
import pe.com.project1.ms.domain.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	@Override
	public Mono<Customer> save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Mono<Customer> findById(String id) {
		return customerRepository.findById(id);
	}

	@Override
	public Mono<Customer> findByDocumentNumber(String documentNumber) {
		return customerRepository.findByDocumentNumber(documentNumber);
	}

	@Override
	public Flux<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Mono<Customer> update(Customer customer, String id) {
		return this.findById(id).map(existingCustomer -> {
			customer.setId(existingCustomer.getId());
			return customer;
		}).flatMap(this::save);
	}

	@Override
	public Mono<Void> deleteById(String id) {
		return customerRepository.deleteById(id);
	}

}
