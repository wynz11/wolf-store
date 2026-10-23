package devpro.example.CommerceAdmin.configurer;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	
	
	public UrlAuthenticationSuccessHandler() {
		super();
	}

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException{
		
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}
	
	protected void handle(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException{
		
		RequestCache requestCache = new HttpSessionRequestCache();
		String targetUrl;
		
		if(response.isCommitted()) {
			return;
		}
		
		// Nếu trước đó user bị redirect sang login từ 1 URL protected
		// thì SavedRequest sẽ không null -> Quay lại URL đó
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null) {
			targetUrl = savedRequest.getRedirectUrl();
		}
		else {
			targetUrl = determineTargetUrl(authentication);
		}
		clearAuthenticationAttributes(request);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	protected String determineTargetUrl(final Authentication authentication)
	throws IllegalStateException{
		final Collection<? extends GrantedAuthority> authorities = 
				authentication.getAuthorities();
		//Lay set ten Role: ["ADMIN", "STAFF", "GUEST",...]
		
		Set<String> roles = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		if (roles.contains("ADMIN")) {
		    return "/admin";
		}

		if (roles.contains("STAFF")) {
		    return "/admin/product";
		}

		return "/home";
	}
	
	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
}
