package pe.com.project1.ms.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	private String id;
	private String names;
	private String firstSurname;
	private String lastSurname;
	private String documentNumber;
	private DocumentType documentType;
	private String email;
	private LocalDate birthdate;
	private CustomerType customerType;
}
