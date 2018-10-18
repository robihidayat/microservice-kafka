package com.robihidayat.microservice.email.event;

import com.robihidayat.microservice.email.EmailServices;
import com.robihidayat.microservice.email.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;

@Component
public class EmailKafkaListener {

    private final Logger log = LoggerFactory.getLogger(EmailKafkaListener.class);

    @Autowired
    EmailServices emailServices;

    @KafkaListener(topics = "order")
    public void order(Invoice invoice, Acknowledgment acknowledgment) throws MessagingException {
        log.info("Revceived Email " + invoice.getId());
        emailServices.sendEmail(invoice.getCustomer().getEmail(), "robismandax3@gmail.com", "<Order Notification>");
        acknowledgment.acknowledge();
    }
}


