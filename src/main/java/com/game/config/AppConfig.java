package com.game.config;

import com.game.repository.IPlayerRepository;
import com.game.repository.PlayerRepositoryMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IPlayerRepository playerRepository() {
        return new PlayerRepositoryMemory();
    }
}