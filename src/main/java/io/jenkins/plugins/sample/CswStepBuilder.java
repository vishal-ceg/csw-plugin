package io.jenkins.plugins.sample;

import hudson.CopyOnWrite;
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
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import io.jenkins.plugins.credentials.CswApiToken;
import io.jenkins.plugins.tools.ScannerInstallation;

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
	  private Gravity gravity = Gravity.LOW;
	  private String cswTokenId;
	  private String scannerInstallation;
	
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

	
      @SuppressWarnings("unused")
	  public String getScannerInstallation() {
	    return scannerInstallation;
	  }

	  @DataBoundSetter
	  public void setScannerInstallation(String scannerInstallation) {
	    this.scannerInstallation = scannerInstallation;
	  }

		
	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
	            System.out.println("Action......."); 
            System.out.println(workspace.toString());
    }

	@Symbol("greet")
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

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


		
        public ListBoxModel doFillGravityItems() {
                ListBoxModel model = new ListBoxModel();
                Stream.of(Gravity.values())
                      .map(Gravity::getGravity)
                      .forEach(model::add);
                return model;
              }
        
        public ListBoxModel doFillCswTokenIdItems(@AncestorInPath Item item, @QueryParameter String cswTokenId) {
            StandardListBoxModel model = new StandardListBoxModel();
            if (item == null) {
              Jenkins jenkins = Jenkins.getInstance();
              if (!jenkins.hasPermission(Jenkins.ADMINISTER)) {
                return model.includeCurrentValue(cswTokenId);
              }
            } else {
              if (!item.hasPermission(Item.EXTENDED_READ) && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
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
