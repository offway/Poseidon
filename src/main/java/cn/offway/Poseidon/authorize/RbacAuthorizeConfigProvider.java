/**
 * 
 */
package cn.offway.Poseidon.authorize;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.vinann.security.core.authorize.AuthorizeConfigProvider;

/**
 * @author wn
 */
@Component
@Order(Integer.MAX_VALUE)
public class RbacAuthorizeConfigProvider implements AuthorizeConfigProvider {

	/* (non-Javadoc)
	 * @see com.vinann.security.core.authorize.AuthorizeConfigProvider#config(org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry)
	 */
	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		config
			.antMatchers(HttpMethod.GET, "/assets/**").permitAll()
			.antMatchers(HttpMethod.GET, "/new/**").permitAll()
			.antMatchers(HttpMethod.GET, "/js/**").permitAll()
			.antMatchers("/notify/**").permitAll()
			.antMatchers(HttpMethod.GET, 
					"/**/*.html",
					"/resource").authenticated()
			.anyRequest()
				.access("@rbacService.hasPermission(request, authentication)");
		return true;
	}

}
