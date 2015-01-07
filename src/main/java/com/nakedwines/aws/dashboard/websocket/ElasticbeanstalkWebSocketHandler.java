package com.nakedwines.aws.dashboard.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket handler for Elasticbeanstalk environment description
 * @author matthewreid
 *
 */
@Component
public class ElasticbeanstalkWebSocketHandler extends TextWebSocketHandler{
	private final ElasticBeanstalkTimer elasticBeanstalkTimer;
	
	@Autowired
	public ElasticbeanstalkWebSocketHandler(ElasticBeanstalkTimer elasticBeanstalkTimer) {
		this.elasticBeanstalkTimer = elasticBeanstalkTimer;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		elasticBeanstalkTimer.addConnection(session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,	CloseStatus status) throws Exception {
		elasticBeanstalkTimer.removeConnection(session);
	}
}
