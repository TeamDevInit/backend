package com.team3.devinit_back.member.repository;

import com.team3.devinit_back.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findBySocialId(String socialId);
    boolean existsByNickName(String nickname);
}
