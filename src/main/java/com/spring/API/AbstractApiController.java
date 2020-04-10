package com.spring.API;

import java.util.Collection;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.spring.Security.UserAccountService;

@Controller
public abstract class AbstractApiController extends ApiValidation {

	@Autowired
	private UserAccountService service;

	protected final Logger log = Logger.getLogger(AbstractApiController.class);

	public boolean checkURL(final Collection<String> urls, final boolean optional) {
		return !urls.isEmpty() ? urls.stream().allMatch(x -> x.startsWith("http://") || x.startsWith("https://"))
				: optional;
	}

	public boolean checkScript(final Collection<String> col) {
		return col.stream().anyMatch(x -> x.length() >= 8 && x.substring(0, 8).equals("<script>"));
	}

	public void authenticateOrUnauthenticate(String username) {
		UserDetails userDetails = username == null ? null : service.loadUserByUsername(username);
		Authentication authenticationToken = new TestingAuthenticationToken(userDetails, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authenticationToken);
	}

}
