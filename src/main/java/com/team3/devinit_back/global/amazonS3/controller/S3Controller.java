package com.team3.devinit_back.global.amazonS3.controller;

import com.team3.devinit_back.global.amazonS3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Operation(
        summary = "단일 파일 업로드",
        description = "단일 파일을 업로드하여 S3에 저장하고, 저장된 파일의 URL을 반환합니다."
    )
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @Operation(
        summary = "다중 파일 업로드",
        description = "여러 파일을 업로드하여 S3에 저장하고, 저장된 각 파일의 URL 목록을 반환합니다."
    )
    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = s3Service.uploadFiles(files);
        return ResponseEntity.ok(fileUrls);
    }

    @Operation(
        summary = "파일 삭제",
        description = "S3에 저장된 파일을 삭제합니다. 삭제할 파일 이름을 파라미터로 전달해야 합니다."
    )
    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        s3Service.deleteFile(fileName);
        return ResponseEntity.ok("파일 삭제 성공");
    }
}