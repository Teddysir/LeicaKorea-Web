package com.example.leica_refactoring.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody RequestMailDto dto) throws Exception {
        String send = mailService.sendMailReject(dto);
        return ResponseEntity.ok("메일이 성공적으로 전송되었습니다!");
    }
}
