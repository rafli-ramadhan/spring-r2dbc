package com.example.spring_app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.pool.PoolingConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.util.StringUtils;

@Slf4j
public class CommonUtil {
    public static String convertJsonToString(Object object) {
        String jsonStr = null;

        try {
            jsonStr = new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(jsonStr, e);
        }

        return jsonStr;
    }

    public static ConnectionFactory getConnectionFactory(R2dbcProperties properties) {
        ConnectionFactoryOptions.Builder builder = ConnectionFactoryOptions.parse(properties.getUrl()).mutate();
        String username = properties.getUsername();
        if (StringUtils.hasText(username)) {
            builder.option(ConnectionFactoryOptions.USER, username);
        }
        String password = properties.getPassword();
        if (StringUtils.hasText(password)) {
            builder.option(ConnectionFactoryOptions.PASSWORD, password);
        }
        String databaseName = properties.getName();
        if (StringUtils.hasText(databaseName)) {
            builder.option(ConnectionFactoryOptions.DATABASE, databaseName);
        }
        if (properties.getPool() != null) {
            builder.option(PoolingConnectionFactoryProvider.INITIAL_SIZE, properties.getPool().getInitialSize());
            builder.option(PoolingConnectionFactoryProvider.MAX_SIZE, properties.getPool().getMaxSize());
        }
        if (properties.getProperties() != null) {
            properties.getProperties()
                    .forEach((key, value) -> builder
                            .option(Option.valueOf(key), value));
        }

        return ConnectionFactories.get(builder.build());
    }
}
