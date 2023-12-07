package com.example.leica_refactoring.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.leica_refactoring.entity.Member;
import com.example.leica_refactoring.enums.UserRole;
import com.example.leica_refactoring.error.exception.requestError.BadRequestException;
import com.example.leica_refactoring.error.exception.requestError.UnAuthorizedException;
import com.example.leica_refactoring.error.security.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {

    private final AmazonS3 s3Client;
    private final MemberService memberService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.default-url}")
    private String defaultUrl;

    public String uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {

        Member member = memberService.findMemberByToken(request);

        if (member.getUserRole() != UserRole.ADMIN) {
            throw new UnAuthorizedException("유저 권한이 없습니다.", ErrorCode.ACCESS_DENIED_EXCEPTION);
        } else {
            UUID uuid = UUID.randomUUID();

            String fileName = uuid.toString();

            String imageUrl = defaultUrl + fileName;
            try {
                s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
                return imageUrl;
            } catch (SdkClientException e) {
                throw new BadRequestException("파일 업로드에 실패했습니다.",ErrorCode.RUNTIME_EXCEPTION);
            }
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

}