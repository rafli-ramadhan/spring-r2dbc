package com.example.spring_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_app.model.Data;
import com.example.spring_app.model.Request;
import com.example.spring_app.service.DBService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    @Autowired
    private DBService service;

    @GetMapping(value = "/v1/content")
    public Mono<ResponseEntity<String>> getContent(@RequestParam(name = "content_id") String contentId) {
        Mono<Data> result = service.getContent(contentId);

        return result
                .flatMap(response -> {
                    if (response != null) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(response.getContentMessage()));
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body("Content not found"));
                    }
                });
    }

    @PostMapping(value = "/v1/content")
    public Mono<ResponseEntity<String>> insertContent(@RequestBody @Valid Request request) {
        return service.insertContent(request).flatMap(isSuccess -> {
            if (isSuccess) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Success"));
            } else {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Failed"));
            }
        });
    }

    @PutMapping(value = "/v1/content/{id}")
    public Mono<ResponseEntity<String>> updateContent(@PathVariable String id, @RequestBody @Valid Request request) {
        return service.updateContent(id, request).flatMap(isSuccess -> {
            if (isSuccess) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Success"));
            } else {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Failed"));
            }
        });
    }

    @DeleteMapping(value = "/v1/content/{id}")
    public Mono<ResponseEntity<String>> deleteContent(@PathVariable String id) {
        return service.deleteContent(id).flatMap(isSuccess -> {
            if (isSuccess) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Success"));
            } else {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body("Failed"));
            }
        });
    }
}
