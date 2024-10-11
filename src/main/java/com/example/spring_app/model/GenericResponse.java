package com.example.spring_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GenericResponse<T> {

    private T response;
    private int httpStatus; 
    
}
