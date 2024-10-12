package com.example.spring_app.service;

import com.example.spring_app.model.Data;
import com.example.spring_app.model.Request;

import reactor.core.publisher.Mono;

public interface DBService {

    Mono<Data> getContent(String contentId);

    Mono<Boolean> insertContent(Request request);

    Mono<Boolean> updateContent(String contentId, Request request);

    Mono<Boolean> deleteContent(String contentId);
}
