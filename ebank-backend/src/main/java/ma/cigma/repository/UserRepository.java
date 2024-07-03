package ma.cigma.repository;

import ma.cigma.entity.User;
import ma.cigma.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  User findByRole(Role role);

  boolean existsByEmail(String email);
}
