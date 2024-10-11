package com.example.spring_app.repository;

import com.example.spring_app.model.Data;

import reactor.core.publisher.Mono;

public interface DBRepo {

    Mono<Data> getContent(String content);

    Mono<Long> insertContent(Data data);

    Mono<Long> updateContent(Data data);

    Mono<Long> deleteContent(Data data);

}
