package io.jenkins.plugins.tools;



import io.jenkins.plugins.sample.CswStepBuilder.DescriptorImpl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import jenkins.model.Jenkins;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolProperty;

public class ScannerInstallation extends ToolInstallation{

	@DataBoundConstructor
	  public ScannerInstallation(@Nonnull String name, @Nonnull String home, List<? extends ToolProperty<?>> properties) {
	    super(name, home, properties);
	  }
	
	@Extension
	  @Symbol("csw")
	  public static class ScannerInstallationDescriptor extends ToolDescriptor<ScannerInstallation> {
		
		  @Nonnull
		    @Override
		    public String getDisplayName() {
		      return "Csw";
		    }
		  
		   @Override
		    public List<? extends ToolInstaller> getDefaultInstallers() {
		      return Collections.singletonList(new ScannerInstaller(null));
		    }
		   
		   @Override
		    public ScannerInstallation[] getInstallations() {
		      Jenkins instance = Jenkins.getInstance();
		      return instance.getDescriptorByType(DescriptorImpl.class).getInstallations();
		    }

		    @Override
		    public void setInstallations(ScannerInstallation... installations) {
		      Jenkins instance = Jenkins.getInstance();
		      instance.getDescriptorByType(DescriptorImpl.class).setInstallations(installations);
		    }

	}



}
