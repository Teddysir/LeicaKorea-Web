package com.example.leica_refactoring.mail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Send Mail Controller", description = "Send Mail API")
@CrossOrigin(origins = {"https://localhost:3000","https://www.nts-microscope.com"})
public class MailController {

    private final MailService mailService;
    @PostMapping("/mail")
    @Operation(summary = "이메일 전송")
    public ResponseEntity<String> sendEmail(@RequestBody RequestMailDto dto) throws Exception {
        String emailSender = mailService.sendMailReject(dto);
        return ResponseEntity.ok("메일이 성공적으로 전송되었습니다!");
    }
}
