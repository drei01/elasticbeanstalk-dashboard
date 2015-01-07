package com.nakedwines.aws.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.nakedwines.aws.dashboard.websocket.ElasticBeanstalkTimer;
import com.nakedwines.aws.dashboard.websocket.ElasticbeanstalkWebSocketHandler;

@Configuration
@EnableAutoConfiguration
@EnableWebSocket
@EnableScheduling
@ComponentScan
public class Application extends SpringBootServletInitializer implements WebSocketConfigurer, SchedulingConfigurer{

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(elasticbeanstalkWebSocketHandler(), "/elasticbeanstalk").withSockJS();
	}
	
	@Bean
	public ElasticBeanstalkTimer elasticBeanstalkTimer(){
		return new ElasticBeanstalkTimer();
	}
	
	@Bean
	public ElasticbeanstalkWebSocketHandler elasticbeanstalkWebSocketHandler(){
		return new ElasticbeanstalkWebSocketHandler(elasticBeanstalkTimer());
	}
	
	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(taskScheduler());		
	}
}
