package com.team3.devinit_back.hub.controller;

import com.team3.devinit_back.hub.dto.HubProfileResponseDto;
import com.team3.devinit_back.hub.service.HubService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hub")
@RequiredArgsConstructor
public class HubController {
    private final HubService hubService;

    @Operation(summary = "허브 조회", description = "기술 스택 및 경력으로 필터링하여 사용자 프로필 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<HubProfileResponseDto>> getFilteredProfiles(
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String employmentPeriod,
            @RequestParam(defaultValue = "latest") String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (employmentPeriod != null) {
            employmentPeriod = URLDecoder.decode(employmentPeriod, StandardCharsets.UTF_8);
        }

        if (skills != null) {
            skills = skills.stream()
                    .map(skill -> URLEncoder.encode(skill, StandardCharsets.UTF_8))
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<HubProfileResponseDto> profiles = hubService.getFilteredProfiles(skills, employmentPeriod, sortType, pageable);
        return ResponseEntity.ok(profiles);
    }
}