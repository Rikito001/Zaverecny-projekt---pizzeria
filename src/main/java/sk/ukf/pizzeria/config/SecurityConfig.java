package sk.ukf.pizzeria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import sk.ukf.pizzeria.security.CustomAuthenticationFailureHandler;
import sk.ukf.pizzeria.security.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider authenticationProvider;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public SecurityConfig(CustomAuthenticationProvider authenticationProvider,
                         CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(authenticationProvider);
        return authBuilder.build();
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
                .failureHandler(authenticationFailureHandler)
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
