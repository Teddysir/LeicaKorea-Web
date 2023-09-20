package com.example.leica_refactoring.mail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RequestMailDto {

    private String content;

    private RequestMailDto(String content){
        this.content = content;
    }

}
