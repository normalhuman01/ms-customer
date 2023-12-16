package pe.com.project1.ms.application;

import pe.com.project1.ms.domain.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

	Mono<Customer> save(Customer customer);

	Mono<Customer> findById(String id);

	Mono<Customer> findByDocumentNumber(String documentNumber);

	Flux<Customer> findAll();

	Mono<Customer> update(Customer customer, String id);

	Mono<Void> deleteById(String id);

}
