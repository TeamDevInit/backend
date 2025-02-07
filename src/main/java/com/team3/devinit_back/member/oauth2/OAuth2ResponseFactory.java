package com.team3.devinit_back.member.oauth2;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.dto.GithubResponse;
import com.team3.devinit_back.member.dto.NaverResponse;
import com.team3.devinit_back.member.dto.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuth2ResponseFactory {

    public OAuth2Response getOAuth2Response(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverResponse(attributes);
            case "github" -> new GithubResponse(attributes);
            default -> throw new CustomException(ErrorCode.INVALID_OAUTH2_PROVIDER);
        };
    }
}