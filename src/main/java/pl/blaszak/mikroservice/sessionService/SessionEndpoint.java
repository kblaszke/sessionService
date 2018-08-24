package pl.blaszak.mikroservice.sessionService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.blaszak.microservice.session.*;
import pl.blaszak.mikroservice.sessionService.service.SessionService;

@Endpoint
public class SessionEndpoint {

    private final ObjectFactory sessionApiObjFactory;

    public static final String NAMESPACE_URI = "http://microservice.blaszak.pl/session";

    private final SessionService sessionService;

    @Autowired
    public SessionEndpoint(ObjectFactory sessionApiObjFactory, SessionService sessionService) {
        this.sessionApiObjFactory = sessionApiObjFactory;
        this.sessionService = sessionService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createSessionRequest")
    @ResponsePayload
    public CreateSessionResponse createSession(@RequestPayload CreateSessionRequest createSessionRequest) {
        CreateSessionResponse response = sessionApiObjFactory.createCreateSessionResponse();
        Long sessionID = sessionService.createSession(createSessionRequest.getTtl());
        response.setSessionId(sessionID);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "checkSessionRequest")
    @ResponsePayload
    public CheckSessionResponse checkSession(@RequestPayload CheckSessionRequest checkSessionRequest) {
        CheckSessionResponse response = sessionApiObjFactory.createCheckSessionResponse();
        SessionState sessionState = sessionService.checkSessionStatus(checkSessionRequest.getSessionId());
        response.setSessionState(sessionState);
        return response;
    }
}
