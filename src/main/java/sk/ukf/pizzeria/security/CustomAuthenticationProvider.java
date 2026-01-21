package sk.ukf.pizzeria.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService, 
                                        PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Toto vyhodí UsernameNotFoundException ak používateľ neexistuje
        // alebo DisabledException ak je účet neaktívny
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (DisabledException e) {
            throw new DisabledException(e.getMessage());
        }

        // Kontrola hesla
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Nesprávne heslo");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails, 
                password, 
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
