package pl.blaszak.mikroservice.sessionService.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger LOGGER = Logger.getLogger(Scheduler.class);

    private final SessionService sessionService;

    public Scheduler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 30000)
    public void removeInactiveSessions() {
        LOGGER.info("Schedule task - delete inactive sessions is running!");
        sessionService.removeInactiveSessions();
    }

}
