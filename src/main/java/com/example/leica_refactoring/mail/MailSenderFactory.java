package com.example.leica_refactoring.mail;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailSenderFactory {
    JavaMailSender getSender(String email,String password);
}
