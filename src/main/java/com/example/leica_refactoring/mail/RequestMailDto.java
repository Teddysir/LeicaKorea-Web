package com.example.leica_refactoring.mail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RequestMailDto {

    private String content;
    private String mailTo;
    private String subject;
    private String mailFrom;

    private RequestMailDto(String content,String mailTo, String subject,String mailFrom){
        this.content = content;
        this.mailTo = mailTo;
        this.subject = subject;
        this.mailFrom = mailFrom;
    }

}
