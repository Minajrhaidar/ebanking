package ma.cigma.dto.user;

import lombok.Data;
import ma.cigma.entity.Roles;
import ma.cigma.entity.enums.Role;
//@Data
//public class UserDTO {
//  private String username;
//  private String password;
//  private Roles role; // Peut être "CLIENT" ou "AGENT_GUICHET"
//}

public record CreateUserRequest(String username, String password, Role role) {
}
