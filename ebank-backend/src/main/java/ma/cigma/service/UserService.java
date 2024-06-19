package ma.cigma.service;

import ma.cigma.dto.user.PermissionVo;
import ma.cigma.dto.user.RoleVo;
import ma.cigma.dto.user.UserVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {


  //void registerUser(UserDTO userDTO);

    //boolean createUser(UserVo userDTO);

    void save(UserVo userVo);

    void save(RoleVo roleVo);

    void save(PermissionVo vo);

    RoleVo getRoleByName(String authority);

    PermissionVo getPermissionByName(String authority);

    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    //Authentication login(LoginRequestDTO loginRequestDTO);
}
