package ma.cigma.controller;

import ma.cigma.dto.user.CreateUserRequest;
import ma.cigma.dto.user.LoginRequestDTO;
import ma.cigma.dto.user.LoginResponseDTO;
import ma.cigma.dto.user.UserVo;
import ma.cigma.service.UserService;
import ma.cigma.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CreateUserRequest createUserRequest) {
        // Déterminez le rôle à attribuer en fonction de ce qui est spécifié dans la requête JSON
        String authority = createUserRequest.role().name(); // Utilisez le nom du rôle directement

        // Créez et sauvegardez l'utilisateur avec le rôle approprié
        userService.save(UserVo.builder()
                .username(createUserRequest.username())
                .password(createUserRequest.password())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .authorities(List.of()) // Ajoutez le rôle directement ici
                .build());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("User [%s] created with success", createUserRequest.username()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);
            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .accessToken(jwt)
                    .username(loginRequestDTO.getUsername())
                    .roles(authentication.getAuthorities()
                            .stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
                    )
                    .build();
            return ResponseEntity.ok(responseDTO);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO("Invalid username or password"));
        }
    }
}
