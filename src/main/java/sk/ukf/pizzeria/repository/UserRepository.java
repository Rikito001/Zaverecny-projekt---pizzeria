package sk.ukf.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.ukf.pizzeria.entity.User;
import sk.ukf.pizzeria.entity.enums.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByActiveTrue();

    List<User> findByRoleAndActiveTrue(Role role);
}
