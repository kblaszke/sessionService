package pl.blaszak.mikroservice.sessionService.service;

import pl.blaszak.microservice.session.SessionState;
import pl.blaszak.mikroservice.sessionService.database.SessionsRepository;
import pl.blaszak.mikroservice.sessionService.database.model.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SessionService {

    public static final long DEFAULT_TTL_MILS = 120000;

    private final SessionsRepository sessionsRepository;

    public SessionService(SessionsRepository sessionsRepository) {
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
}
