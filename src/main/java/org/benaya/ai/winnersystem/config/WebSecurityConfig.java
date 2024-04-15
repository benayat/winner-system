package org.benaya.ai.winnersystem.config;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.handler.SseLogoutHandler;
import org.benaya.ai.winnersystem.service.impl.CustomJpaUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomJpaUserDetailsServiceImpl userDetailsService;
    private final SseLogoutHandler sseLogoutHandler;
    private final CorsConfigurationSourceImpl corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/team/all", "/api/team/sorted").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/sse-events/round").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/season/is-active").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/signup").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/user/signup").permitAll()
                        .requestMatchers("/api/season/start", "/api/user/logged-in").permitAll()
                        .requestMatchers("/api/team/**", "/api/season/start", "api/sse-events/bets", "/api/user/update-email", "/api/user/delete", "/api/user/update-username").hasRole("USER")
                        .anyRequest().authenticated())
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .key("my key")
                        .tokenValiditySeconds(1800)
                        .userDetailsService(userDetailsService))
//                .httpBasic(httpBasic -> httpBasic.realmName("winnersystem"))
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/login")
                        .permitAll()
                        .defaultSuccessUrl("/api/user/")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .addLogoutHandler(sseLogoutHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
