package ma.cigma.security.service.impl;

import lombok.RequiredArgsConstructor;
import ma.cigma.repository.UserRepository;
import ma.cigma.security.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;



    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username){
                return userRepository.findByEmail(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));

            }
        };
    }

    @Override
    public boolean isValidEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
