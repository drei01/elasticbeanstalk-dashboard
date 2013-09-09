package com.nakedwines.aws.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.elasticbeanstalk.model.ApplicationDescription;
import com.amazonaws.services.elasticbeanstalk.model.ApplicationVersionDescription;
import com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsResult;
import com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationsResult;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentResourcesRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsResult;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentResourceDescription;

@Service
public class ElasticBeanstalkService {
	private AWSElasticBeanstalkClient awsElasticBeanstalkAsyncClient = new AWSElasticBeanstalkClient(new BasicAWSCredentials(System.getProperty("awsKey"), System.getProperty("awsSecret")));
	
	private Region region = RegionUtils.getRegion("eu-west-1");
	
	public List<EnvironmentDescription> describeEnvironments(){
		awsElasticBeanstalkAsyncClient.setRegion(region);
		DescribeEnvironmentsResult result =  awsElasticBeanstalkAsyncClient.describeEnvironments();
		return result.getEnvironments();
	}
	
	public List<ApplicationDescription> describeApplications(){
		awsElasticBeanstalkAsyncClient.setRegion(region);
		DescribeApplicationsResult result = awsElasticBeanstalkAsyncClient.describeApplications();
		return result.getApplications();
	}
	
	public EnvironmentResourceDescription  describeApplicationResources(String environmentId){
		awsElasticBeanstalkAsyncClient.setRegion(region);
		return awsElasticBeanstalkAsyncClient.describeEnvironmentResources(
					new DescribeEnvironmentResourcesRequest().withEnvironmentId(environmentId)).getEnvironmentResources();
	}
	
	public List<ApplicationVersionDescription> getVersions(String applicationName){
		DescribeApplicationVersionsRequest request = new DescribeApplicationVersionsRequest().withApplicationName(applicationName);
		DescribeApplicationVersionsResult result = awsElasticBeanstalkAsyncClient.describeApplicationVersions(request);
		return result.getApplicationVersions();
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
