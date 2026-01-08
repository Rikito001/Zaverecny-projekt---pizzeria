package sk.ukf.pizzeria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Verejne URL
                .requestMatchers("/", "/pizza/**", "/ponuka/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/registracia", "/prihlasenie", "/error/**").permitAll()
                
                // Kosik a objednavky - len prihlaseni
                .requestMatchers("/kosik/**").authenticated()
                .requestMatchers("/objednavka/nova", "/objednavka/vytvorit").authenticated()
                .requestMatchers("/moje-objednavky/**").authenticated()
                .requestMatchers("/profil/**").authenticated()
                
                // Kuchyna - len kuchar a admin
                .requestMatchers("/kuchyna/**").hasAnyRole("KUCHAR", "ADMIN")
                
                // Rozvoz - len kurier a admin
                .requestMatchers("/rozvoz/**").hasAnyRole("KURIER", "ADMIN")
                
                // Admin sekcia
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/prihlasenie")
                .loginProcessingUrl("/prihlasenie")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/prihlasenie?chyba=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/odhlasenie")
                .logoutSuccessUrl("/?odhlaseny=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        return http.build();
    }
}
