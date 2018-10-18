package com.robihidayat.microservice.email;

import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Service
public class EmailServices {

    private final Logger log = LoggerFactory.getLogger(EmailServices.class);

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(String to, String from, String subject) throws MessagingException {
        Context context = new Context();
        String content = templateEngine.process("orderEmail", context);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
        helper.setTo(to);
        helper.setFrom(from);
        helper.setText(content, true);
        helper.setSubject(subject);
        log.debug("Send email to: {}", to);
        emailSender.send(mimeMessage);
    }

}
