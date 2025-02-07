package com.team3.devinit_back.global.amazonS3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value(("${aws.s3.default-profile-image}"))
    private String defaultProfileImageUrl;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String getDefaultProfileImageUrl() {
        return defaultProfileImageUrl;
    }

    public boolean isDefaultProfileImage(String imageUrl) {
        return imageUrl != null && imageUrl.equals(getDefaultProfileImageUrl());
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        try {
            List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                fileUrls.add(uploadFile(file));
            }
            return fileUrls;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID() + "-" + originalFilename;
    }
}