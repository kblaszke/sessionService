package pl.blaszak.mikroservice.sessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.blaszak.microservice.session.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.blaszak.mikroservice.sessionService.database.SessionsRepository;
import pl.blaszak.mikroservice.sessionService.service.SessionService;

@Configuration
public class SessionServiceConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public ObjectFactory sessionObjectFactory() {
        return new ObjectFactory();
    }

    @Bean
    public SessionService getSessionService(SessionsRepository sessionsRepository) {
        return new SessionService(jdbcTemplate, sessionsRepository);
    }
}
