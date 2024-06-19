package ma.cigma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  private String password;
  @OneToMany(mappedBy = "user")
  private List<AccountOperation> bankAccountTransactionList;
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "USER_ROLE")
  private List<Roles> authorities = new ArrayList<>();
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;
}
