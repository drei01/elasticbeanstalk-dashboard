package com.nakedwines.aws.dashboard.bo;

import java.util.List;

import com.amazonaws.services.elasticbeanstalk.model.ApplicationVersionDescription;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentResourceDescription;

public class Environment extends EnvironmentDescription{
	private static final long serialVersionUID = 1L;
	private EnvironmentResourceDescription envResources;
	private List<ApplicationVersionDescription> versions;

	public EnvironmentResourceDescription getEnvResources() {
		return envResources;
	}

	public void setEnvResources(EnvironmentResourceDescription envResources) {
		this.envResources = envResources;
	}

	public List<ApplicationVersionDescription> getVersions() {
		return versions;
	}

	public void setVersions(List<ApplicationVersionDescription> versions) {
		this.versions = versions;
	}
}
