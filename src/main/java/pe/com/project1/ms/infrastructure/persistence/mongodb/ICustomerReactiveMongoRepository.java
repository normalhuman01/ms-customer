package pe.com.project1.ms.infrastructure.persistence.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import pe.com.project1.ms.infrastructure.persistence.model.CustomerDao;
import reactor.core.publisher.Mono;

public interface ICustomerReactiveMongoRepository extends ReactiveMongoRepository<CustomerDao, String> {

	Mono<CustomerDao> findByDocumentNumber(String documentNumber);

}
