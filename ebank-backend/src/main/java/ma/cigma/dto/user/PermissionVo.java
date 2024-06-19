package ma.cigma.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//this field contains the authority, for example : GET_ALL_CUSTUMERS

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionVo {
    private int id;
    private String authority;
}