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
                        .requestMatchers(HttpMethod.GET, "api/sse-events/round").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/signup").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/user/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/game/start").permitAll()
                        .requestMatchers("/api/team/**").hasRole("USER")
                        .requestMatchers("/api/game/start").hasRole("USER")
                        .requestMatchers("/api/sse-events/bets").hasRole("USER")
                        .requestMatchers("/api/user/update-email").hasRole("USER")
                        .requestMatchers("/api/user/delete").hasRole("USER")
                        .requestMatchers("/api/user/update-username").hasRole("USER")
                        .anyRequest().authenticated())
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
