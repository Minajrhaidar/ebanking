package ma.cigma.repository;

import ma.cigma.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByCin(String cin);
    Optional<Customer> findByEmail(String email);

    List<Customer> findByNameContains(String keyword);
}