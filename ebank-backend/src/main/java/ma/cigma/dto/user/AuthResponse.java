package ma.cigma.dto.user;

import lombok.Data;

@Data

public class AuthResponse {

  private String token;
  private String refreshToken;
}
