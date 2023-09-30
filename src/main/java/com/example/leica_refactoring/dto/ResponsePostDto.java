package com.example.leica_refactoring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.sql.Timestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponsePostDto {

    private Long id;
    private String title;
    private String content;
    private String subTitle;
    private String thumbnail;
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime modified_at;

}
