package io.jenkins.plugins.credentials;

import hudson.Util;
import hudson.util.Secret;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardCredentials;

@NameWith(value = CswApiToken.NameProvider.class, priority = 1)
public interface CswApiToken extends StandardCredentials {
	
	@Nonnull
	Secret getToken() throws IOException, InterruptedException;

	class NameProvider extends CredentialsNameProvider<CswApiToken> {

		@Nonnull
		@Override
		public String getName(@Nonnull CswApiToken credentials) {
			String description = Util.fixEmptyAndTrim(credentials
					.getDescription());
			return description != null ? description : credentials.getId();
		}
	}

}
