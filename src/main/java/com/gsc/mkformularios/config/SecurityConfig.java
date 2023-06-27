package com.gsc.mkformularios.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsc.mkformularios.config.environment.EnvironmentConfig;
import com.gsc.mkformularios.repository.toyota.ClientRepository;
import com.gsc.mkformularios.repository.toyota.ConfigurationRepository;
import com.gsc.mkformularios.security.JwtAuthenticationFilter;
import com.gsc.mkformularios.security.JwtAuthenticationManager;
import com.gsc.mkformularios.security.RestAuthenticationEntryPoint;
import com.gsc.mkformularios.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Value("${server.servlet.context-path}")
    private static String baseUrl;


    private static final String[] AUTH_WHITELIST = {
            "/sign-in",
            "/sign-in/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger/**",
            "/v2/api-docs/**",
            "/swagger-cust/**",
            baseUrl+"/swagger-ui/**",
            baseUrl+"/docs/**",
            baseUrl+"/docs"
    };



    @Bean
    public AuthenticationManager authenticationManager(ConfigurationRepository configurationRepository,
                                                       EnvironmentConfig environmentConfig,
                                                       ClientRepository clientRepository) throws Exception {
        return new JwtAuthenticationManager(configurationRepository, environmentConfig, clientRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${app.auth.token-name}") String tokenName, TokenProvider tokenProvider, ObjectMapper objectMapper) throws Exception {
        http
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint(objectMapper));

        http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider, tokenName), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> customCorsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setName("CorsConfiguration");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}

