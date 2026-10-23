package devpro.example.CommerceAdmin.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class SecureConfigurer {

	@Bean
	public AuthenticationSuccessHandler urlAuthenticationSuccessHandler() {
		return new UrlAuthenticationSuccessHandler();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Tat CSRF
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/UploadFiles/**", "/login", "/static/**", "/logout").permitAll()
								// .requestMatchers("/admin/**").authenticated()
								.requestMatchers("/admin/product/**").hasAnyAuthority("ADMIN", "STAFF")
								.requestMatchers("/admin/**").hasAuthority("ADMIN")
								.requestMatchers("/staff/**").hasAnyAuthority("ADMIN", "STAFF")
								.anyRequest().permitAll())

				// Giu lai phan requestCache nhu cu
				.requestCache(cache -> cache.requestCache(new HttpSessionRequestCache()))

				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login_processing_url")
						// .defaultSucessUrl("/admin/home", true)
						.successHandler(urlAuthenticationSuccessHandler()).failureUrl("/login?login_error=true")
						.permitAll())

				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID").permitAll());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new UrlAuthenticationSuccessHandler();
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder(4).encode("123"));
	}

}
