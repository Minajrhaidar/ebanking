package ma.cigma.security.service;


import ma.cigma.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {

    String generateRefreshToken(Map<String, Object> extractClaims, User userDetails);

    String extractUsername(String token);
    String generateToken(User userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);



}