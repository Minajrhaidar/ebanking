package ma.cigma.repository;

import ma.cigma.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByAuthority(String authority);
    List<Roles> findAll();

}