package pl.blaszak.mikroservice.sessionService;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.blaszak.microservice.session.SessionState;
import pl.blaszak.mikroservice.sessionService.service.SessionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionServiceTests implements WithAssertions {

	@Autowired
	private SessionService sessionService;

	@Test
	public void shouldReturnSessionOkStatus() {
		// given
		Long sessionId = sessionService.createSession(null);
		// when
		SessionState sessionState = sessionService.checkSessionStatus(sessionId);
		// then
		assertThat(sessionState).isEqualTo(SessionState.REFRESHED);
	}

	@Test
	public void shouldReturnSessionExpiredStatusForExpiredSession() throws InterruptedException {
		// given
		Long sessionId = sessionService.createSession(1000L);
		// when
		Thread.sleep(1001L);
		SessionState sessionState = sessionService.checkSessionStatus(sessionId);
		// then
		assertThat(sessionState).isEqualTo(SessionState.EXPIRED);
	}

	@Test
	public void shoudReturnSessionNotFoundForNotInitializedSession() {
		// given
		Long sessionId = sessionService.createSession(null) + 5;
		// when
		SessionState sessionState = sessionService.checkSessionStatus(sessionId);
		// then
		assertThat(sessionState).isEqualTo(SessionState.NOT_FOUND);
	}
}
