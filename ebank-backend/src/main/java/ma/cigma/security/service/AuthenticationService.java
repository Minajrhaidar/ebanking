package ma.cigma.security.service;

import ma.cigma.dto.user.AuthRequest;
import ma.cigma.dto.user.AuthResponse;
import ma.cigma.dto.user.RefreshTokenRequest;
import ma.cigma.entity.User;

public interface AuthenticationService {
    User signup(AuthRequest authRequest);

    AuthResponse signin(AuthRequest authRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}