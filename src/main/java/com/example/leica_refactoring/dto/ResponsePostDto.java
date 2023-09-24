package com.example.leica_refactoring.dto;

import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.sql.Timestamp;

@Data
@Builder
public class ResponsePostDto {

    private Long id;
    private String title;
    private String subTitle;
    private String thumbnail;
    private String category;
    private Timestamp create_at;
    private Timestamp modified_at;

}
