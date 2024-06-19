package ma.cigma.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

  private String accessToken;
  private String username;

  private List<String> roles= new ArrayList<>();

  public LoginResponseDTO(String accessToken) {
    this.accessToken = accessToken;
  }


}
