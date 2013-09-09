package com.nakedwines.aws.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.nakedwines.aws.dashboard.bo.Environment;
import com.nakedwines.aws.dashboard.service.ElasticBeanstalkService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private ElasticBeanstalkService elasticBeanstalkService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("regions", RegionUtils.getRegions());
		return "index";
	}
	
	@RequestMapping(value="/elasticbeanstalk")
	@ResponseBody
	public void elasticBeanstalkAsync(AtmosphereResource atmosphereResource){
		this.suspend(atmosphereResource);

	    final Broadcaster bc = atmosphereResource.getBroadcaster();
	    
	    logger.info("Atmo Resource Size: " + bc.getAtmosphereResources().size());

	    bc.scheduleFixedBroadcast(new Callable<String>() {
	        @Override
	        public String call() throws Exception {
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

	    }, 30, TimeUnit.SECONDS);
	}
	
	@RequestMapping(value="region",method=RequestMethod.POST)
	@ResponseBody
	public void setRegion(@RequestParam String region){
		elasticBeanstalkService.setRegion(RegionUtils.getRegion(region));
	}
	
	private void suspend(final AtmosphereResource resource) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                countDownLatch.countDown();
                resource.removeEventListener(this);
            }
        });
        resource.suspend();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
