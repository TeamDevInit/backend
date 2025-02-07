package com.team3.devinit_back.board.init;

import com.team3.devinit_back.board.entity.Category;
import com.team3.devinit_back.board.entity.Tag;
import com.team3.devinit_back.board.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
@RequiredArgsConstructor
public class TagInitializer implements CommandLineRunner {

    private final TagRepository tagRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultTags = List.of("프론트엔드", "백엔드", "Spring", "React",
                "JS", "TS", "Java", "C++", "C", "C#", "Python", "R", "Web", "AWS", "GCP", "Cloud",
                "인턴", "부트캠프", "코딩테스트", "정규직", "계약직", "이직", "경력직","신입");



        for (String tagName : defaultTags) {
            if (!tagRepository.existsByName(tagName)) {
                tagRepository.save(new Tag(tagName));
            }
        }
    }
}
