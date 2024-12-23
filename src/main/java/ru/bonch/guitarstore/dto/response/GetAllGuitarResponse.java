package ru.bonch.guitarstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAllGuitarResponse (

        @JsonProperty("guitars")
        List<GetGuitarResponse> guitars
) {}
