package com.team3.devinit_back.member.service;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.entity.Member;
import com.team3.devinit_back.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean isNicknameExists(String nickname) { return memberRepository.existsByNickName(nickname);}

    public boolean updateNicknameBySocailId(String socialId, String nickname){
        if(isNicknameExists(nickname)){ throw new CustomException(ErrorCode.DUPLICATED_NAME); }

        Member member =  findMemberBySocialId(socialId);
        if (member == null) { throw new CustomException(ErrorCode.INVALID_USER); }

        member.setNickName(nickname);
        memberRepository.save(member);
        return true;
    }

    public Member findMemberBySocialId(String socialId){
        return memberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
