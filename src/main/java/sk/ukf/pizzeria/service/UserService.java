package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.User;
import sk.ukf.pizzeria.entity.enums.Role;
import sk.ukf.pizzeria.exception.EmailAlreadyExistsException;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pouzivatel s ID " + id + " neexistuje"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " uz je registrovany");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ZAKAZNIK);
        user.setActive(true);
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser) {
        User user = findById(id);
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setPostalCode(updatedUser.getPostalCode());
        return userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword) {
        User user = findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void updateRole(Long id, Role role) {
        User user = findById(id);
        user.setRole(role);
        userRepository.save(user);
    }

    public void toggleActive(Long id) {
        User user = findById(id);
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("Pouzivatel s ID " + id + " neexistuje");
        }
        userRepository.deleteById(id);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> findActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
