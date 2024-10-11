package com.example.spring_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    @NotBlank
    @JsonProperty("content_id")
    private String contentID;
    
    @NotEmpty
    @JsonProperty("content_message")
    private String contentMessage;
}
