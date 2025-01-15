package com.team3.devinit_back.member.repository;

import com.team3.devinit_back.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findBySocialId(String socialId);
    boolean existsByNickName(String nickname);
}
