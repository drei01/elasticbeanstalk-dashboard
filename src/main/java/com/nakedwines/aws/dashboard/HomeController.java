package com.nakedwines.aws.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.regions.RegionUtils;
import com.nakedwines.aws.dashboard.service.ElasticBeanstalkService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {	
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
	
	@RequestMapping(value="region",method=RequestMethod.POST)
	@ResponseBody
	public void setRegion(@RequestParam String region){
		elasticBeanstalkService.setRegion(RegionUtils.getRegion(region));
	}
}
