package pe.com.project1.ms.application.persistence;

import pe.com.project1.ms.domain.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

	Mono<Customer> save(Customer customer);

	Mono<Customer> findById(String id);

	Mono<Customer> findByDocumentNumber(String documentNumber);

	Flux<Customer> findAll();

	Mono<Void> deleteById(String id);

}
