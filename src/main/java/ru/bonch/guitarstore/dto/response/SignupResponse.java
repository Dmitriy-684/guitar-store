package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SignupResponse(
        @JsonProperty("username")
        String username,

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("phone")
        String phone,

        @JsonProperty("full_name")
        String fullName
) {}
