package io.jenkins.plugins.sample;

import hudson.CopyOnWrite;
import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Item;
import hudson.model.TaskListener;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolInstallerDescriptor;
import hudson.tools.ToolProperty;
import hudson.tools.InstallSourceProperty;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.credentials.CswApiToken;
import io.jenkins.plugins.tools.Scanner;
import io.jenkins.plugins.tools.ScannerInstallation;
import io.jenkins.plugins.tools.ScannerInstaller;
import io.jenkins.plugins.tools.ScannerInstaller.ScannerInstallerDescriptor;

import java.io.IOException;
import java.util.stream.Stream;

import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class CswStepBuilder extends Builder implements SimpleBuildStep {

	private boolean failOnIssues = true;
	private boolean sast = false;
	private boolean dast = false;
	private boolean oss = false;
	private boolean container = false;
	private String sastInstallation;
	private String dastInstallation;
	private String ossInstallation;
	private String containerInstallation;
	private Gravity gravity = Gravity.LOW;
	private String cswTokenId;

	@DataBoundConstructor
	public CswStepBuilder() {
		// called from stapler
	}

	@SuppressWarnings("unused")
	public boolean isFailOnIssues() {
		return failOnIssues;
	}

	@DataBoundSetter
	public void setFailOnIssues(boolean failOnIssues) {
		this.failOnIssues = failOnIssues;
	}

	@SuppressWarnings("unused")
	public boolean getSast() {
		return sast;
	}

	@DataBoundSetter
	public void setSast(boolean sast) {
		this.sast = sast;

	}

	@SuppressWarnings("unused")
	public boolean getOss() {
		return oss;
	}

	@DataBoundSetter
	public void setOss(boolean oss) {
		this.oss = oss;
	}

	@SuppressWarnings("unused")
	public boolean getContainer() {
		return container;
	}

	@DataBoundSetter
	public void setContainer(boolean container) {
		this.container = container;
	}

	@SuppressWarnings("unused")
	public boolean getDast() {
		return dast;
	}

	@DataBoundSetter
	public void setDast(boolean dast) {
		this.dast = dast;
	}

	@SuppressWarnings("unused")
	public String getSastInstallation() {
		return this.sastInstallation;
	}

	@SuppressWarnings("unused")
	public String getDastInstallation() {
		return this.dastInstallation;
	}

	@SuppressWarnings("unused")
	public String getOssInstallation() {
		return this.ossInstallation;
	}

	@SuppressWarnings("unused")
	public String getContainerInstallation() {
		return this.containerInstallation;
	}

	@DataBoundSetter
	public void setSastInstallation(String sastInstallation) {
		this.sastInstallation = sastInstallation;
	}

	@DataBoundSetter
	public void setDastInstallation(String dastInstallation) {
		this.dastInstallation = dastInstallation;
	}

	@DataBoundSetter
	public void setOssInstallation(String ossInstallation) {
		this.ossInstallation = ossInstallation;
	}

	@DataBoundSetter
	public void setContainerInstallation(String containerInstallation) {
		this.containerInstallation = containerInstallation;
	}

	@SuppressWarnings("unused")
	public String getGravity() {
		return gravity != null ? gravity.getGravity() : null;
	}

	@DataBoundSetter
	public void setGravity(String gravity) {
		this.gravity = Gravity.getIfPresent(gravity);
	}

	@SuppressWarnings("unused")
	public String getCswTokenId() {
		return cswTokenId;
	}

	@DataBoundSetter
	public void setCswTokenId(String cswTokenId) {
		this.cswTokenId = cswTokenId;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher,
			TaskListener listener) throws InterruptedException, IOException {
		System.out.println("Business logic .......");
	}

	@Symbol("greet")
	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {

		@CopyOnWrite
		private volatile ScannerInstallation[] installations = new ScannerInstallation[0];

		public DescriptorImpl() {
			load();
		}

		@SuppressFBWarnings("EI_EXPOSE_REP")
		public ScannerInstallation[] getInstallations() {
			return installations;
		}

		public void setInstallations(ScannerInstallation... installations) {
			this.installations = installations;
			save();
		}

		public boolean hasInstallationsAvailable() {
			return installations.length > 0;
		}

		public ListBoxModel doFillSastInstallationItems() {
			ListBoxModel model = new ListBoxModel();
			for (ScannerInstallation scannerInstaller : installations) {
				if (isApplicableFor(Scanner.SAST, scannerInstaller)) {
					model.add(scannerInstaller.getName());
				}
			}
			if (model.isEmpty()) {
				model.add("Please define a scanner installation in the Jenkins Global Tool Configuration.");
			}
			return model;
		}

		public ListBoxModel doFillDastInstallationItems() {
			ListBoxModel model = new ListBoxModel();
			for (ScannerInstallation scannerInstaller : installations) {
				if (isApplicableFor(Scanner.DAST, scannerInstaller)) {
					model.add(scannerInstaller.getName());
				}
			}
			if (model.isEmpty()) {
				model.add("Please define a scanner installation in the Jenkins Global Tool Configuration.");
			}

			return model;
		}

		public ListBoxModel doFillOssInstallationItems() {
			ListBoxModel model = new ListBoxModel();
			for (ScannerInstallation scannerInstaller : installations) {
				if (isApplicableFor(Scanner.OSS, scannerInstaller)) {
					model.add(scannerInstaller.getName());
				}
			}
			if (model.isEmpty()) {
				model.add("Please define a scanner installation in the Jenkins Global Tool Configuration.");
			}

			return model;

		}

		public ListBoxModel doFillContainerInstallationItems() {
			ListBoxModel model = new ListBoxModel();
			for (ScannerInstallation scannerInstaller : installations) {
				if (isApplicableFor(Scanner.CONTAINER, scannerInstaller)) {
					model.add(scannerInstaller.getName());
				}
			}
			if (model.isEmpty()) {
				model.add("Please define a scanner installation in the Jenkins Global Tool Configuration.");
			}

			return model;

		}

		private static boolean isApplicableFor(Scanner scanner,
				ScannerInstallation scannerInstallation) {
			for (ToolProperty<?> t : scannerInstallation.getProperties()) {
				if (t instanceof InstallSourceProperty) {
					InstallSourceProperty obj = (InstallSourceProperty) t;
					if (obj.installers == null || obj.installers.isEmpty()) {
						return false;
					}
					for (ToolInstaller tool : obj.installers) {
						if (tool instanceof ScannerInstaller) {
							ScannerInstaller scannerInstaller = (ScannerInstaller) tool;
							if (scanner.getScanner().equalsIgnoreCase(
									scannerInstaller.getScanner())) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		public ListBoxModel doFillGravityItems() {
			ListBoxModel model = new ListBoxModel();
			Stream.of(Gravity.values()).map(Gravity::getGravity)
					.forEach(model::add);
			return model;
		}

		public ListBoxModel doFillCswTokenIdItems(@AncestorInPath Item item,
				@QueryParameter String cswTokenId) {
			StandardListBoxModel model = new StandardListBoxModel();
			if (item == null) {
				Jenkins jenkins = Jenkins.getInstance();
				if (!jenkins.hasPermission(Jenkins.ADMINISTER)) {
					return model.includeCurrentValue(cswTokenId);
				}
			} else {
				if (!item.hasPermission(Item.EXTENDED_READ)
						&& !item.hasPermission(CredentialsProvider.USE_ITEM)) {
					return model.includeCurrentValue(cswTokenId);
				}
			}
			return model.includeEmptyValue()
					.includeAs(ACL.SYSTEM, item, CswApiToken.class)
					.includeCurrentValue(cswTokenId);
		}

		public FormValidation doCheckCswTokenId(@QueryParameter String value) {
			return FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Csw Configuration";
		}
	}
}
