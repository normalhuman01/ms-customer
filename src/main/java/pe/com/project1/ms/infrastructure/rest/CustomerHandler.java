package pe.com.project1.ms.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import pe.com.project1.ms.application.CustomerService;
import pe.com.project1.ms.domain.Customer;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
	
	private final CustomerService customerService;
	
	public Mono<ServerResponse> postCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class)
				.map(customerService::save)
				.flatMap(customer -> this.toServerResponse(customer, HttpStatus.CREATED));
	}

	public Mono<ServerResponse> getCustomerById(ServerRequest request) {
		Mono<Customer> customer = customerService.findById(request.pathVariable("id"));
		return this.toServerResponse(customer, HttpStatus.OK);
	}
	
	public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
		Flux<Customer> customers = customerService.findAll();
		return this.toServerResponse(customers, HttpStatus.OK);
	}

	public Mono<ServerResponse> putCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class)
				.map(customer -> customerService.update(customer, request.pathVariable("id")))
				.flatMap(customer -> this.toServerResponse(customer, HttpStatus.OK));
	}
	
	public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
		return customerService.deleteById(request.pathVariable("id"))
				.then(ServerResponse.noContent().build());
	}
	
	private Mono<ServerResponse> toServerResponse(CorePublisher<Customer> customer, HttpStatus status) {
		return ServerResponse
				.status(status)
				.contentType(MediaType.APPLICATION_JSON)
				.body(customer, Customer.class);
	}
}
