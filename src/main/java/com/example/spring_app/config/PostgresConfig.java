package com.example.spring_app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import com.example.spring_app.util.CommonUtil;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfig {

	@Bean
	@ConfigurationProperties(prefix="spring.postgre")
	@Qualifier("postgreProperties")
	public R2dbcProperties postgreProperties() {
		return new R2dbcProperties();
	}

	@Bean()
	@Qualifier("postgreConnectionFactory")
	@ConfigurationProperties(prefix="spring.postgre")
	public ConnectionFactory postgreConnectionFactory(@Qualifier("postgreProperties") R2dbcProperties properties) {
		return CommonUtil.getConnectionFactory(properties);
	}

	@Bean
	@Qualifier("postgreEntityContent")
	public R2dbcEntityTemplate postgreEntityContent(@Qualifier("postgreConnectionFactory") ConnectionFactory connectionFactory) {
		DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE);
		DatabaseClient databaseClient = DatabaseClient.builder()
				.connectionFactory(connectionFactory)
				.bindMarkers(PostgresDialect.INSTANCE.getBindMarkersFactory())
				.build();

		return new R2dbcEntityTemplate(databaseClient, strategy);
	}

}