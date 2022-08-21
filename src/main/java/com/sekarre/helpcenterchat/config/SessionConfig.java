package com.sekarre.helpcenterchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
public class SessionConfig {

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(50 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(50 * 1024 * 1024);
        return container;
    }
}
