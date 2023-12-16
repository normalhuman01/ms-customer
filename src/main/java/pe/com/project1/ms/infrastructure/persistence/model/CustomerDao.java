package pe.com.project1.ms.infrastructure.persistence.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.project1.ms.domain.Customer;
import pe.com.project1.ms.domain.CustomerType;
import pe.com.project1.ms.domain.DocumentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("customers")
public class CustomerDao {
	@Id
	private String id;
	private String names;
	private String firstSurname;
	private String lastSurname;
	@Indexed(unique = true)
	private String documentNumber;
	private DocumentType documentType;
	private String email;
	private LocalDate birthdate;
	private CustomerType customerType;

	public CustomerDao(Customer customer) {
		id = customer.getId();
		names = customer.getNames();
		firstSurname = customer.getFirstSurname();
		lastSurname = customer.getLastSurname();
		documentNumber = customer.getDocumentNumber();
		documentType = customer.getDocumentType();
		email = customer.getEmail();
		birthdate = customer.getBirthdate();
		customerType = customer.getCustomerType();
	}

	public Customer toCustomer() {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setNames(names);
		customer.setFirstSurname(firstSurname);
		customer.setLastSurname(lastSurname);
		customer.setDocumentNumber(documentNumber);
		customer.setDocumentType(documentType);
		customer.setEmail(email);
		customer.setBirthdate(birthdate);
		customer.setCustomerType(customerType);
		return customer;
	}

	public CustomerDao(String names, String firstSurname, String lastSurname, String documentNumber,
			DocumentType documentType, String email, LocalDate birthdate, CustomerType customerType) {
		super();
		this.names = names;
		this.firstSurname = firstSurname;
		this.lastSurname = lastSurname;
		this.documentNumber = documentNumber;
		this.documentType = documentType;
		this.email = email;
		this.birthdate = birthdate;
		this.customerType = customerType;
	}

}
