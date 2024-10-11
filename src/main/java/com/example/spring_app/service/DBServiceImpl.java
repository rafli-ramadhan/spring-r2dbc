package com.example.spring_app.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.spring_app.model.Data;
import com.example.spring_app.model.Request;
import com.example.spring_app.repository.DBRepo;
import com.example.spring_app.util.CommonUtil;
import com.example.spring_app.util.Logger;

import reactor.core.publisher.Mono;

@Service
public class DBServiceImpl implements DBService {

    long startTime = System.currentTimeMillis();

    private static final Logger LOG = Logger.getInstance();

    @Autowired
    private DBRepo repo;

    @Autowired
    private DBRepo postgreRepo;

    @Override
    public Mono<Data> getContent(String contentId) {
        return repo.getContent(contentId)
                .flatMap(response -> {
                    LOG.logTrace("getContent", startTime, contentId, CommonUtil.convertJsonToString(response), HttpStatus.OK);
                    return Mono.just(response);
                });
    }

    @Override
    public Mono<Long> insertContent(Request request) {
        Data data = new Data(request.getContentID(), request.getContentMessage(), false, new Date(), null);

        return repo.insertContent(data)
                .flatMap(response -> {
                    LOG.logTrace("insertContent", startTime, CommonUtil.convertJsonToString(request), CommonUtil.convertJsonToString(response), HttpStatus.OK);
                    return postgreRepo.insertContent(data)
                            .flatMap(response2 -> Mono.just(1L));
                })
                .onErrorResume(ex -> {
                    LOG.logException("insertContent", startTime, CommonUtil.convertJsonToString(request), CommonUtil.convertJsonToString(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR, ex);
                    return postgreRepo.insertContent(data)
                            .flatMap(response2 -> Mono.error(ex));
                });
    }

    @Override
    public Mono<Long> updateContent(String contentId, Request request) {
        Data data = new Data(contentId, request.getContentMessage(), false, null, new Date());

        return repo.updateContent(data)
                .flatMap(response -> {
                    LOG.logTrace("updateContent", startTime, CommonUtil.convertJsonToString(request), CommonUtil.convertJsonToString(response), HttpStatus.OK);
                    return postgreRepo.updateContent(data)
                            .flatMap(response2 -> Mono.just(1L));
                })
                .onErrorResume(ex -> {
                    LOG.logException("insertContent", startTime, CommonUtil.convertJsonToString(request), CommonUtil.convertJsonToString(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR, ex);
                    return postgreRepo.insertContent(data)
                            .flatMap(response2 -> Mono.error(ex));
                });
    }

    @Override
    public Mono<Long> deleteContent(String contentId) {
        Data data = new Data(contentId, null, true, null, null);

        return repo.deleteContent(data)
                .flatMap(response -> {
                    LOG.logTrace("deleteContent", startTime, contentId, CommonUtil.convertJsonToString(response), HttpStatus.OK);
                    return postgreRepo.deleteContent(data)
                            .flatMap(response2 -> Mono.just(1L));
                })
                .onErrorResume(ex -> {
                    LOG.logException("insertContent", startTime, contentId, CommonUtil.convertJsonToString(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR, ex);
                    return postgreRepo.insertContent(data)
                            .flatMap(response2 -> Mono.error(ex));
                });
    }

}