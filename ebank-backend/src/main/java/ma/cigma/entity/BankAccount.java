package ma.cigma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.cigma.entity.enums.AccountStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String rib; // RIB
    private double balance; // Solde
    private AccountStatus status; // Statut (Ouvert, Fermé, etc.)

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}