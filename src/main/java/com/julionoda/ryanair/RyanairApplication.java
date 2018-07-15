package com.julionoda.ryanair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;

@SpringBootApplication
@EnableCaching
@EnableCircuitBreaker
public class RyanairApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RyanairApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RyanairApplication.class, args);
	}

	/**
	 * Customized formatter for {@link LocalDateTime}
	 * 
	 * @return
	 */
	@Bean
	public Formatter<LocalDateTime> localDateTimeFormatter() {
		return new Formatter<LocalDateTime>() {
			private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {
				return LocalDateTime.parse(text, formatter);
			}

			@Override
			public String print(LocalDateTime object, Locale locale) {
				return formatter.format(object);
			}
		};
	}
}
