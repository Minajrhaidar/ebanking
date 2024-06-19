package ma.cigma.service.impl;

import lombok.AllArgsConstructor;
import ma.cigma.dto.user.PermissionVo;
import ma.cigma.dto.user.RoleVo;
import ma.cigma.dto.user.UserVo;
import ma.cigma.entity.Permission;
import ma.cigma.entity.Roles;
import ma.cigma.entity.User;
import ma.cigma.repository.PermissionRepository;
import ma.cigma.repository.RoleRepository;
import ma.cigma.repository.UserRepository;
import ma.cigma.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//public class UserServiceImpl implements UserService {
//  private final UserRepository userRepository;
//  private final PasswordEncoder passwordEncoder;
//  private final RoleRepository roleRepository;
//  private final ModelMapper modelMapper;
//
//  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ModelMapper modelMapper) {
//    this.userRepository = userRepository;
//    this.passwordEncoder = passwordEncoder;
//      this.roleRepository = roleRepository;
//      this.modelMapper = modelMapper;
//  }
//
//
//  @Override
//  public boolean createUser(UserVo userDTO){
//    //check if user already exist
//    if(userRepository.existsByUsername(userDTO.getUsername())){
//      return false;
//    }
//    User user=new User();
//    BeanUtils.copyProperties(userDTO, user);
//    // Hash the password
//    String hashPassword = passwordEncoder.encode(userDTO.getPassword());
//    user.setPassword(hashPassword);
//    userRepository.save(user);
//    return true;
//  }
//  @Override
//  public void save(UserVo userVo) {
//    User user = modelMapper.map(userVo, User.class);
//    user.setPassword(passwordEncoder.encode(user.getPassword()));
//    user.setAuthorities(user.getAuthorities().stream().map(bo ->
//                    roleRepository.findByAuthority(bo.getAuthority()).get()).
//            collect(Collectors.toList()));
//    userRepository.save(user);
//  }
//
////  @Override
////  public void registerUser(UserDTO userDTO) {
////    User user = new User();
////    user.setUsername(userDTO.getUsername());
////    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
////    user.setRole(userDTO.getRole());
////    userRepository.save(user);
////  }
//
//
//
//}


@Service
@Transactional
@AllArgsConstructor
@Primary
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = modelMapper.map(userRepository.findByUsername(username), UserVo.class);
        List<RoleVo> permissions = new ArrayList<>();
        userVo.getAuthorities().forEach(
                roleVo -> roleVo.getAuthorities().forEach(
                        permission -> permissions.add(
                                RoleVo.builder().authority(permission.getAuthority()).build())));
        userVo.getAuthorities().addAll(permissions);
        return userVo;
    }

    @Override
    public void save(UserVo userVo) {
        User user = modelMapper.map(userVo, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(user.getAuthorities().stream().map(bo ->
                        roleRepository.findByAuthority(bo.getAuthority()).get()).
                collect(Collectors.toList()));
        userRepository.save(user);
    }

    @Override
    public void save(RoleVo roleVo) {
        Roles role = modelMapper.map(roleVo, Roles.class);
        role.setAuthorities(role.getAuthorities().stream().map(bo ->
                        permissionRepository.findByAuthority(bo.getAuthority()).get()).
                collect(Collectors.toList()));
        roleRepository.save(role);
    }

    @Override
    public void save(PermissionVo vo) {
        permissionRepository.save(modelMapper.map(vo, Permission.class));
    }

    @Override
    public RoleVo getRoleByName(String authority) {
        return modelMapper.map(roleRepository.findByAuthority(authority).get(), RoleVo.class);
    }

    @Override
    public PermissionVo getPermissionByName(String authority) {
        return modelMapper.map(permissionRepository.findByAuthority(authority), PermissionVo.class);
    }
}
