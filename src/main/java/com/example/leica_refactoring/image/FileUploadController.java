package com.example.leica_refactoring.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FileUploadController {

    private final S3FileUploadService s3FileUploadService;

    @PostMapping
    public String uploadFile(@RequestPart("file")MultipartFile[] files) throws IOException {
//        if(files == null || files.length == 0 ){
//            return ResponseEntity.ok("이미지를 업로드하지 않았습니다.");
//        }
        String key = s3FileUploadService.uploadFile(files[0]);
        return key; // key값 리턴해줄거임

        // postman으로 form-data 형식으로 key는 "file"로 해주고 이미지 파일 선택해서 post로 데이터 전송하면 key-"파일명.type" 형식으로 반환될거임

    }
}
