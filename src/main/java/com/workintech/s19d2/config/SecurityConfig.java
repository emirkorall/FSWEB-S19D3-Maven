package com.workintech.s19d2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }


    //DATABASE LOGIN//
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);

    }


    //SECURITY RULES FOR HTTP REQUESTS//
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/register").permitAll();
                    auth.requestMatchers("/welcome/").permitAll();
                    auth.requestMatchers("/account/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/account/**").hasAnyAuthority("USER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/account/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/account/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/account/**").hasAnyAuthority("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();

    }
}
