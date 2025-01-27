package com.team3.devinit_back.global.amazonS3.controller;

import com.team3.devinit_back.global.amazonS3.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 단일 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = s3Service.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        s3Service.deleteFile(fileName);
        return ResponseEntity.ok("파일 삭제 성공");
    }
}