package com.ewolff.microservice.order.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerTestDataGenerator {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerTestDataGenerator(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@PostConstruct
	public void generateTestData() {
		customerRepository
				.save(new Customer("Robi", "Hidayat", "robismandax3@gmail.com", "Jalan Suanan Gunung Jati", "Cirebon"));
		customerRepository.save(new Customer("Rizky", "Hidayat", "robihidayat122@gmail.com", "Jakarta Selatan", "San Francisco"));
	}

}
