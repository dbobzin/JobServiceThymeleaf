//package com.tutorial.securingwebtutorial.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Override
//    public void sendSimpleMessage(String to, String subject, String text) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text, true);
//
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            // Handle exception (log it or throw a custom exception)
//            e.printStackTrace();
//        }
//    }
//}
//
