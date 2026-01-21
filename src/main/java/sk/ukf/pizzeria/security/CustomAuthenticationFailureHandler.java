package sk.ukf.pizzeria.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage;
        
        if (exception instanceof DisabledException) {
            errorMessage = "Účet neaktívny";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Používateľ neexistuje";
        } else if (exception instanceof BadCredentialsException) {
            // BadCredentialsException sa vyhodí aj keď používateľ neexistuje (Spring Security skrýva túto info)
            // Preto musíme skontrolovať pôvodnú príčinu
            Throwable cause = exception.getCause();
            if (cause instanceof UsernameNotFoundException) {
                errorMessage = "Používateľ neexistuje";
            } else if (cause instanceof DisabledException) {
                errorMessage = "Účet neaktívny";
            } else {
                errorMessage = "Nesprávne heslo";
            }
        } else {
            errorMessage = "Nesprávne prihlasovacie údaje";
        }
        
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        response.sendRedirect("/prihlasenie?chyba=" + encodedMessage);
    }
}
