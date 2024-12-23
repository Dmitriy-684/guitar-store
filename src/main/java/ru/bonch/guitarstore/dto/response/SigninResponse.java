package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SigninResponse(

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("role")
        String role,

        @JsonProperty("token")
        String token
) {}
