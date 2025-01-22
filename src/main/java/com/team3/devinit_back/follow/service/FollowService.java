package com.team3.devinit_back.follow.service;

import com.team3.devinit_back.follow.dto.FollowCountResponse;
import com.team3.devinit_back.follow.entity.Follow;
import com.team3.devinit_back.follow.repository.FollowRepository;
import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void follow(String senderId, String receiverId) {
        validateFollowRequest(senderId, receiverId);
        validateFollowRelation(senderId, receiverId, true);

        Follow follow = new Follow();
        follow.setSenderId(senderId);
        follow.setReceiverId(receiverId);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String senderId, String receiverId) {
        validateFollowRequest(senderId, receiverId);
        validateFollowRelation(senderId, receiverId, false);

        followRepository.deleteBySenderIdAndReceiverId(senderId, receiverId);
    }

    @Transactional(readOnly = true)
    public FollowCountResponse getFollowCounts(String memberId) {
        int followerCount = followRepository.countByReceiverId(memberId);
        int followingCount = followRepository.countBySenderId(memberId);
        return new FollowCountResponse(followerCount, followingCount);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(String senderId, String receiverId) {
        return followRepository.existsBySenderIdAndReceiverId(senderId, receiverId);
    }

    private void validateFollowRequest(String senderId, String receiverId) {
        if (!profileRepository.existsById(receiverId)) {
            throw new CustomException(ErrorCode.PROFILE_NOT_FOUND);
        }

        if (senderId.equals(receiverId)) {
            throw new CustomException(ErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }
    }

    private void validateFollowRelation(String senderId, String receiverId, boolean isFollowRequest) {
        if (isFollowRequest && followRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new CustomException(ErrorCode.FOLLOW_FAILED);
        }

        if (!isFollowRequest && !followRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new CustomException(ErrorCode.FOLLOW_FAILED);
        }
    }
}