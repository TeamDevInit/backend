package com.team3.devinit_back.member.service;

import com.team3.devinit_back.member.dto.MemberDto;
import com.team3.devinit_back.member.entity.MemberEntity;
import com.team3.devinit_back.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //중복 닉네임 검증
    public boolean isNicknameExists(String nickname) { return memberRepository.existsByNickName(nickname);}

    //닉네임 변경
    public boolean updateNicknameBySocailId(String socialId, String nickname){
        MemberEntity member =  memberRepository.findBySocialId(socialId);
        if (member == null) {
            throw new IllegalArgumentException("해당 ID에 해당하는 회원을 찾을 수 없습니다: " + socialId);
        }

        member.setNickName(nickname);
        memberRepository.save(member);
        return true;
    }
}
