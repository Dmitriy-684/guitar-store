package ru.bonch.guitarstore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignupRequest(

        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password,

        @JsonProperty("phone")
        String phone,

        @JsonProperty("full_name")
        String fullName
) {}
