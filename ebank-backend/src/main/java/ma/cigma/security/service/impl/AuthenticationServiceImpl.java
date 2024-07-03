package ma.cigma.security.service.impl;


import lombok.RequiredArgsConstructor;
import ma.cigma.dto.user.AuthRequest;
import ma.cigma.dto.user.AuthResponse;
import ma.cigma.dto.user.RefreshTokenRequest;
import ma.cigma.entity.User;
import ma.cigma.entity.enums.Role;
import ma.cigma.repository.UserRepository;
import ma.cigma.security.service.AuthenticationService;
import ma.cigma.security.service.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User signup(AuthRequest authRequest){
        User user= new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.ROLE_CLIENT);
        return userRepository.save(user);
    }


    @Override
    public AuthResponse signin(AuthRequest authRequest){
        authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
        var user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("Invalid email or password!"));
        var jwt= jwtService.generateToken(user);
        var refreshToken= jwtService.generateRefreshToken(new HashMap<>(), user);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setToken(jwt);
        authResponse.setRefreshToken(refreshToken);
        return authResponse;

    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail=jwtService.extractUsername(refreshTokenRequest.getToken());
        User user=userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt= jwtService.generateToken(user);

            AuthResponse authResponse=new AuthResponse();
            authResponse.setToken(jwt);
            authResponse.setRefreshToken(refreshTokenRequest.getToken());
            return authResponse;
        }
        return null;
    }
}