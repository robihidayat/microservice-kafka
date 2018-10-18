package com.robihidayat.microservice.email.event;


import com.robihidayat.microservice.email.Invoice;
import org.springframework.kafka.support.serializer.JsonDeserializer;


public class EmailDeserializer extends JsonDeserializer<Invoice> {

	public EmailDeserializer() {
		super(Invoice.class);
	}

}
