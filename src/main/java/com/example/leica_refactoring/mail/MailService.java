//package com.example.leica_refactoring.mail;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MailService {
//    private final JavaMailSender javaMailSender;
//    @Transactional
//    public String sendMailReject(RequestMailDto dto) throws Exception{
//
//        try {
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//
//            simpleMailMessage.setTo(dto.getMailTo());
//            simpleMailMessage.setSubject("[Leica] 견적 요청 메일입니다.");
//            simpleMailMessage.setText(dto.getContent());
//
//            javaMailSender.send(simpleMailMessage);
//
//            return "성공적으로 메일이 전송되었습니다.";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "메일 전송중 오류가 발생하였습니다.";
//        }
//    }
//}
