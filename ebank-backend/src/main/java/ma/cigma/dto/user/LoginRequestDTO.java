package ma.cigma.dto.user;

import lombok.Data;

@Data
public class LoginRequestDTO {
  private String username;
  private String password;
}
