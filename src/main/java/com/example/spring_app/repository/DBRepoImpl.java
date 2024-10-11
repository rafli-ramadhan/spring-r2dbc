package com.example.spring_app.repository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;

import com.example.spring_app.config.AppConfig;
import com.example.spring_app.model.Data;

import reactor.core.publisher.Mono;

@Repository
public class DBRepoImpl implements DBRepo {

    @Autowired
    @Qualifier("postgreEntityContent")
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    AppConfig config;

    @Override
    public Mono<Data> getContent(String contentId) {
        String query = "select * from content where content_id = $1";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind(0, contentId)
                .map(row -> new Data(
                        row.get("content_id", String.class),
                        row.get("content_message", String.class),
                        row.get("is_delete", Boolean.class),
                        row.get("created_date", Date.class),
                        row.get("updated_date", Date.class)))
                .one()
                .defaultIfEmpty(new Data(null, null, null, null, null));
    }

    @Override
    public Mono<Long> insertContent(Data data) {
        String query = "insert into content(content_id, content_message, is_delete, created_date, updated_date) values ($1, $2, $3, $4, $5)";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind(0, data.getContentId())
                .bind(1, data.getContentMessage())
                .bind(2, data.getIsDeleted())
                .bind(3, data.getCreatedDate())
                .bindNull(4, Date.class)
                .fetch()
                .rowsUpdated();
    }

    @Override
    public Mono<Long> updateContent(Data data) {
        String query = "update content set content_message = $1, updated_date = $2 where content_id = $3";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind(0, data.getContentMessage())
                .bind(1, data.getUpdatedDate())
                .bind(2, data.getContentId())
                .fetch()
                .rowsUpdated();
    }

    @Override
    public Mono<Long> deleteContent(Data data) {
        String query = "delete from content where content_id = $1";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind(0, data.getContentId())
                .fetch()
                .rowsUpdated();
    }

}
