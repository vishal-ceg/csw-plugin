package io.jenkins.plugins.credentials;

import hudson.Extension;
import hudson.util.Secret;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;

public class CswApiTokenImpl extends BaseStandardCredentials implements
		CswApiToken {

	@Nonnull
	private final Secret token;

	@DataBoundConstructor
	public CswApiTokenImpl(CredentialsScope scope, String id,
			String description, @Nonnull String token) {
		super(scope, id, description);
		this.token = Secret.fromString(token);
	}

	@Override
	public Secret getToken() throws IOException, InterruptedException {
		return token;
	}
	
	@Extension
	public static class CswApiTokenDescriptor extends BaseStandardCredentialsDescriptor {

	    @Nonnull
	    @Override
	    public String getDisplayName() {
	      return "CSW API Token";
	    }
	  }

}
