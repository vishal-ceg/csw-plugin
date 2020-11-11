package io.jenkins.plugins.sample;

import hudson.model.Run;
import jenkins.model.RunAction2;

public class CswAction implements RunAction2 {
	private transient Run run;
	private String projectPath;

	public CswAction(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public Run getRun() {
		return run;
	}

	@Override
	public void onAttached(Run<?, ?> run) {
		this.run = run;
	}

	@Override
	public void onLoad(Run<?, ?> run) {
		this.run = run;
	}

	@Override
	public String getIconFileName() {
		return "document.png";
	}

	@Override
	public String getDisplayName() {
		return "Greeting";
	}

	@Override
	public String getUrlName() {
		return "greeting";
	}
}
