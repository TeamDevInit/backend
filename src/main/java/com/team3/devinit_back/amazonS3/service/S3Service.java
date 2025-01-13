package com.team3.devinit_back.amazonS3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 파일 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

        System.out.println("Uploaded file: " + fileName);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    //다중 파일 업로드
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            fileUrls.add(uploadFile(file));
        }
        return fileUrls;
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
        System.out.println("Deleting file: " + fileName);
    }

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "-" + originalFilename;
    }
}