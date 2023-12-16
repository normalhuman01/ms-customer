package pe.com.project1.ms.infrastructure.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CustomerRouter {

	private static final String CUSTOMERS = "/customers";
	private static final String CUSTOMER_ID = CUSTOMERS + "/{id}";

	@Bean
	public RouterFunction<ServerResponse> routes(CustomerHandler customerHandler) {
		return route(POST(CUSTOMERS).and(accept(APPLICATION_JSON)), customerHandler::postCustomer)
				.andRoute(GET(CUSTOMERS).and(accept(APPLICATION_JSON)), customerHandler::getAllCustomers)
				.andRoute(GET(CUSTOMER_ID).and(accept(APPLICATION_JSON)), customerHandler::getCustomerById)
				.andRoute(PUT(CUSTOMER_ID).and(accept(APPLICATION_JSON)), customerHandler::putCustomer)
				.andRoute(DELETE(CUSTOMER_ID).and(accept(APPLICATION_JSON)), customerHandler::deleteCustomer);
	}
}
