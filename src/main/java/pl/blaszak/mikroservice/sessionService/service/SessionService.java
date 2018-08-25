package pl.blaszak.mikroservice.sessionService.service;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.blaszak.microservice.session.SessionState;
import pl.blaszak.mikroservice.sessionService.database.SessionsRepository;
import pl.blaszak.mikroservice.sessionService.database.model.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SessionService {

    public static final long DEFAULT_TTL_MILS = 120000;

    private final JdbcTemplate jdbcTemplate;
    private final SessionsRepository sessionsRepository;

    public SessionService(JdbcTemplate jdbcTemplate, SessionsRepository sessionsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionsRepository = sessionsRepository;
    }

    public Long createSession(Long ttl){
        Session session = sessionsRepository.save(createNewSession(ttl == null ? DEFAULT_TTL_MILS : ttl));
        return session.getId();
    }

    private Session createNewSession(Long ttl) {
        Session session = new Session();
        session.setLastRefreshed(LocalDateTime.now());
        session.setTtl(ttl);
        return session;
    }

    public SessionState checkSessionStatus(Long sessionId) {
        Session session = sessionsRepository.findById(sessionId).orElse(null);
        if(session == null) return SessionState.NOT_FOUND;
        LocalDateTime now = LocalDateTime.now();
        Long liveTime = Timestamp.valueOf(now).getTime() - Timestamp.valueOf(session.getLastRefreshed()).getTime();
        if(liveTime > session.getTtl()) {
            sessionsRepository.delete(session);
            return SessionState.EXPIRED;
        }
        session.setLastRefreshed(now);
        sessionsRepository.save(session);
        return SessionState.REFRESHED;
    }

    public void removeInactiveSessions() {
        String DELETE_QUERY = "DELETE FROM session WHERE DATE_ADD(last_refreshed, interval FLOOR(ttl/1000) second) < NOW()";
        jdbcTemplate.execute(DELETE_QUERY);
    }
}
