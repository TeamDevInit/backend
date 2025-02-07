package com.team3.devinit_back.member.dto;

import java.util.Map;

public class GithubResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "github";
    }

    @Override
    public String getProviderId() {

        return attribute.get("login").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }
}
