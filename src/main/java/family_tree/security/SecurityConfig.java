package family_tree.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())                     // for development; -> in serious testing enable it
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/verify", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                      // URL login page
                        .loginProcessingUrl("/login")             // URL для обробки POST запиту
                        .usernameParameter("username")            // name variable email in form
                        .passwordParameter("password")            // name variable password in form
                        .defaultSuccessUrl("/", true)       // where to redirect after success login.
                        .failureUrl("/login?error=true")         // where to redirect when page have errors
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .httpBasic(basic -> basic.disable());             // відключити basic auth (опційно)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}