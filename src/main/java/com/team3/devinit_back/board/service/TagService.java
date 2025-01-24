package com.team3.devinit_back.board.service;

import com.team3.devinit_back.board.entity.Tag;
import com.team3.devinit_back.board.repository.BoardRepository;
import com.team3.devinit_back.board.repository.TagRepository;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final BoardRepository boardRepository;

    public List<Tag> getAllTag(){
        return tagRepository.findAll();
    }

    public Tag findTag(String tagName){
        return tagRepository.findByName(tagName)
                .orElseThrow(() -> new CustomException(ErrorCode.TAG_NOT_FOUND));
    }
}
