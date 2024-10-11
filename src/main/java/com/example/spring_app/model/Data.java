package com.example.spring_app.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Data {
    private String contentId;
    private String contentMessage;
    private Boolean isDeleted;
    private Date createdDate;
    private Date updatedDate;
}
