package ru.bonch.guitarstore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SigninRequest(

        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password
) {}
