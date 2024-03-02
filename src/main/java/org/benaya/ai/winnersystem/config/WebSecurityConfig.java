package org.benaya.ai.winnersystem.config;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.service.CustomJpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomJpaUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/client/byEmail", "/client/byId", "/client/byFirstName", "/client/byPhone").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/client/add", "/client/delete").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

//    @Bean
//    UserDetailsManager usersDetailsManager(DataSource dataSource) {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//                .roles("USER", "ADMIN")
//                .build();
//        JdbcUserDetailsManager usersManager = new JdbcUserDetailsManager(dataSource);
//        usersManager.createUser(admin);
//        return usersManager;
//    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        return new InMemoryUserDetailsManager(
//                User.withUsername("user")
//                        .password(passwordEncoder().encode("userpass"))
//                        .roles("USER")
//                        .build(),
//                User.withUsername("admin")
//                        .password(passwordEncoder().encode("adminpass"))
//                        .roles("ADMIN")
//                        .build());
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
