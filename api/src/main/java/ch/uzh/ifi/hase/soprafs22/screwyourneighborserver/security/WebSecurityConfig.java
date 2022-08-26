package ch.uzh.ifi.hase.soprafs22.screwyourneighborserver.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${spring.h2.console.path}")
  private String h2ConsolePath;

  @Value("${security.cors.allowed-hosts}")
  private String[] allowedHosts;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .configurationSource(
            request -> {
              CorsConfiguration corsConfiguration = new CorsConfiguration();
              corsConfiguration.setAllowCredentials(true);
              corsConfiguration.setAllowedMethods(List.of(CorsConfiguration.ALL));
              var allowedOrigins = Arrays.stream(allowedHosts).collect(Collectors.toList());
              corsConfiguration.setAllowedOrigins(allowedOrigins);
              corsConfiguration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
              return corsConfiguration;
            });

    http.csrf().disable();

    http.headers()
        .contentSecurityPolicy(
            "form-action 'self' http://localhost:8080 screw-your-neighbor-server.herokuapp.com")
        .and()
        .frameOptions()
        .sameOrigin();

    http.sessionManagement(
        sessionConfig -> {
          sessionConfig.maximumSessions(1);
          sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
          sessionConfig.enableSessionUrlRewriting(false);
        });

    http.exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/auth/**",
            "/players",
            "/swagger-ui*",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            h2ConsolePath + "/**")
        .permitAll()
        .anyRequest()
        .authenticated();

    http.logout().disable();
  }
}
