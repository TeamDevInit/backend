package com.team3.devinit_back.follow.repository;

import com.team3.devinit_back.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, String> {
    boolean existsBySenderIdAndReceiverId(String senderId, String receiverId); // 이미 팔로우 상태인지 확인

    void deleteBySenderIdAndReceiverId(String senderId, String receiverId); // 팔로우 취소

    int countByReceiverId(String receiverId); // 팔로워 수 계산

    int countBySenderId(String senderId); // 팔로잉 수 계산
}
