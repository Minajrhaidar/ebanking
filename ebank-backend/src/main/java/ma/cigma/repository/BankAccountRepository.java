package ma.cigma.repository;

import ma.cigma.entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {

    Optional<BankAccount> findByRib(String rib);
    List<BankAccount> findByCustomerId(Long customerId);


    Optional<BankAccount> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    Page<BankAccount> searchByRibContainingIgnoreCase(String keyword, Pageable pageable);

}