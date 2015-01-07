package com.nakedwines.aws.dashboard.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakedwines.aws.dashboard.bo.Environment;
import com.nakedwines.aws.dashboard.service.ElasticBeanstalkService;

public class ElasticBeanstalkTimer {
	private final ObjectMapper mapper = new ObjectMapper();
	
	private static final ConcurrentHashMap<String,WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();
	
	@Autowired
	private ElasticBeanstalkService elasticBeanstalkService;
	
	public void addConnection(WebSocketSession session){
		sessions.put(session.getId(),session);
	}
	
	public void removeConnection(WebSocketSession session){
		sessions.remove(session.getId());
	}
	
	@Scheduled(fixedDelay=30000)
	public void sendMessages() throws MessagingException, JsonProcessingException {
		String message = getBeanstalkEnvironmentResponse();
		for(String i : sessions.keySet()){
			WebSocketSession session = sessions.get(i);
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getBeanstalkEnvironmentResponse() throws JsonProcessingException{
		List<Environment> envs = new ArrayList<Environment>();
    	for(EnvironmentDescription desc : elasticBeanstalkService.describeEnvironments()){
    		Environment env = new Environment();
    		BeanUtils.copyProperties(desc, env);
    		env.setEnvResources(elasticBeanstalkService.describeApplicationResources(desc.getEnvironmentId()));
    		env.setVersions(elasticBeanstalkService.getVersions(desc.getApplicationName()));
    		envs.add(env);
    	}
        return mapper.writeValueAsString(envs);
	}
}
