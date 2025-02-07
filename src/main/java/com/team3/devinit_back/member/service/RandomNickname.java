package com.team3.devinit_back.member.service;

import com.team3.devinit_back.member.repository.NicknameAdjectiveRepository;
import com.team3.devinit_back.member.repository.NicknameNounRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Random;


// 랜덤닉네임 생성 클래스
@Service
@RequiredArgsConstructor
public class RandomNickname {
    private final NicknameAdjectiveRepository adjectiveRepository;
    private final NicknameNounRepository nounRepository;
    private final Random random = new Random();

    public String generate() {

        //String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        int randomInt = random.nextInt(99999) + 1;
        //String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
        String adjective = adjectiveRepository.getRandomAdjective().getValue();
        String noun = nounRepository.getRandomNoun().getValue();
        return MessageFormat.format("{0} {1}번째 {2}", adjective, randomInt, noun);
    }
}