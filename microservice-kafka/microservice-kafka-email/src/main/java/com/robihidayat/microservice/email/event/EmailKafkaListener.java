package com.robihidayat.microservice.email.event;

import com.robihidayat.microservice.email.EmailServices;
import com.robihidayat.microservice.email.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class EmailKafkaListener {

    private final Logger log = LoggerFactory.getLogger(EmailKafkaListener.class);

    @Autowired
    EmailServices emailServices;

    @KafkaListener(topics = "order")
    public void order(Invoice invoice, Acknowledgment acknowledgment) {
        log.info("Revceived Email " + invoice.getId());
        emailServices.sendSimpleMessage(invoice.getCustomer().getEmail(), "Test Email", invoice.getInvoiceLine().toString());
        acknowledgment.acknowledge();
    }
}
