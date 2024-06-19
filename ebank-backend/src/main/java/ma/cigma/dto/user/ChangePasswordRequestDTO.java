package ma.cigma.dto.user;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
  private String oldPassword;
  private String newPassword;
}
