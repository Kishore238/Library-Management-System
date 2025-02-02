package com.practice.LBM;

import com.practice.LBM.manager.UserManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/book")
                                .permitAll()
                                .requestMatchers("/api/**")
                                .authenticated()
                ).formLogin(x-> x.disable())
                .cors(x->x.disable())
                .csrf(x->x.disable())
                .build();

    };
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        UserDetails userDetails= User.builder()
                .username("lib")
                .password(passwordEncoder().encode("password"))
                .roles("LIB")
                .build();
//        UserDetails userDetails2= User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("MEM")
//                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserManagerImpl userManager(){
        return new UserManagerImpl();
    }


    @Bean
    AuthenticationManager authenticationManager(){
        AuthenticationProvider inMemoryauthenticationProvider= authenticationProvider(inMemoryUserDetailsManager());
        AuthenticationProvider dbAuthenticationProvider = authenticationProvider(userManager());
        return new ProviderManager(inMemoryauthenticationProvider,dbAuthenticationProvider);
    }
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                if(authentication == null){
                    throw new AuthenticationCredentialsNotFoundException("no authentication");
                }
                UsernamePasswordAuthenticationToken token=(UsernamePasswordAuthenticationToken) authentication;
                String inUsername= token.getName();
                String inPassword= token.getCredentials().toString();
                if(inUsername==""){
                    throw new AuthenticationCredentialsNotFoundException("no authentication");

                }
                UserDetails user = userDetailsService.loadUserByUsername(inUsername);
                if (!passwordEncoder().matches(inPassword,user.getPassword())){
                    throw new AuthenticationCredentialsNotFoundException("invalid creds");
                }
                return new UsernamePasswordAuthenticationToken(inUsername,inPassword,user.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }
}
