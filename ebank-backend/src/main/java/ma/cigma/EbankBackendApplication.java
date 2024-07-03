package ma.cigma;


import ma.cigma.entity.User;
import ma.cigma.entity.enums.Role;
import ma.cigma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class EbankBackendApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        User chefAccount= userRepository.findByRole(Role.ROLE_AGENT_GUICHET);
        if(null == chefAccount) {
            User user = new User();
            user.setEmail("mina@gmail.com");
            user.setPassword(new BCryptPasswordEncoder().encode("12345"));
            user.setRole(Role.ROLE_AGENT_GUICHET);
            userRepository.save(user);
        }

    }
}
