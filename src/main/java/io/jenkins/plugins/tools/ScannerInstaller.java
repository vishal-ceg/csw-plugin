package io.jenkins.plugins.tools;


import io.jenkins.plugins.sample.Gravity;

import java.io.IOException;
import java.util.stream.Stream;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.model.Node;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstallerDescriptor;
import hudson.util.ListBoxModel;

public class ScannerInstaller extends ToolInstaller {

	  private Scanner scanner = Scanner.SAST;
	
	  @DataBoundConstructor
	  public ScannerInstaller(String label) {
	    super(label);
	 }
	  
	  @SuppressWarnings("unused")
	  public String getScanner() {
	    return scanner == null ? null : scanner.getScanner();
	  }
	  
	  @DataBoundSetter
	  public void setScanner(String scanner) {
	    this.scanner = Scanner.getIfPresent(scanner);
	  }

	@Override
	public FilePath performInstallation(ToolInstallation tool, Node node,
			TaskListener log) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("Installation logic........");
		return null;
	}
	
	 @Extension
	  public static final class ScannerInstallerDescriptor extends ToolInstallerDescriptor<ScannerInstaller> {

	    @Override
	    public String getDisplayName() {
	      return "Add scanner";
	    }
	    
	    
	    
	    @Override
	    public boolean isApplicable(Class<? extends ToolInstallation> toolType) {
	      return toolType == ScannerInstallation.class;
	    }
	    
	    public ListBoxModel doFillScannerItems() {
            ListBoxModel model = new ListBoxModel();
            Stream.of(Scanner.values())
                  .map(Scanner::getScanner)
                  .forEach(model::add);
            return model;
          }
	  }


}
