package com.team3.devinit_back.follow.service;

import com.team3.devinit_back.follow.entity.Follow;
import com.team3.devinit_back.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    @Transactional
    public void follow(String senderId, String receiverId) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        if (followRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new IllegalArgumentException("이미 팔로우 중입니다.");
        }

        Follow follow = new Follow();
        follow.setSenderId(senderId);
        follow.setReceiverId(receiverId);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String senderId, String receiverId) {
        followRepository.deleteBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Transactional(readOnly = true)
    public int getFollowerCount(String memberId) {
        return followRepository.countByReceiverId(memberId);
    }

    @Transactional(readOnly = true)
    public int getFollowingCount(String memberId) {
        return followRepository.countBySenderId(memberId);
    }
}