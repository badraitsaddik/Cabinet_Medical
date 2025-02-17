package ma.enset.springmvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class securityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password(passwordEncoder.encode("0987"))
                        .authorities("USER")
                        .build(),
                User.withUsername("user1")
                        .password(passwordEncoder.encode("0987"))
                        .authorities("USER")
                        .build(),
                User.withUsername("user2")
                        .password(passwordEncoder.encode("0987"))
                        .roles("USER","ADMIN")
                        .build()
                );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin((formLogin) ->
                formLogin.loginPage("/login").permitAll()
        )
                .rememberMe(withDefaults())
                .authorizeHttpRequests( authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/webjars/**").permitAll()
                        )
                /*.authorizeHttpRequests( authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                        )*/
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .anyRequest().authenticated()
                )
                .exceptionHandling( exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/notAuthorized"));
        return http.build();
    }
}

