package com.example.spring_app.service;

import com.example.spring_app.model.Data;
import com.example.spring_app.model.Request;

import reactor.core.publisher.Mono;

public interface DBService {

    Mono<Data> getContent(String contentId);

    Mono<Long> insertContent(Request request);

    Mono<Long> updateContent(String contentId, Request request);

    Mono<Long> deleteContent(String contentId);
}
