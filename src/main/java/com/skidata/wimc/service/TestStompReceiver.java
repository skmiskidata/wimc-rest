package com.skidata.wimc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;

/**
 * Created by Mihael on 5. 11. 2017.
 */
@Component
public class TestStompReceiver extends StompSessionHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TestStompReceiver.class);

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
        super.handleTransportError(session, exception);
    }

//    @PostConstruct
//    public void init() {
//        WebSocketClient webSocketClient = new StandardWebSocketClient();
//        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
//
//        String url = "ws://127.0.0.1:8080/gs-guide-websocket";
//        stompClient.connect(url, this);
//        logger.info("started stomp client...");
//    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/hello", this);
        //session.send("/app/hello", "{\"name\":\"Client\"}".getBytes());

        logger.info("New session: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("Received: {}", (payload));
    }
}

