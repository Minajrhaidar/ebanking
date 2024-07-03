//package ma.cigma.integration.controller;
//
//import ma.cigma.dto.BankAccountRequestDTO;
//import ma.cigma.entity.BankAccount;
//import ma.cigma.repository.BankAccountRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Collections;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AccountRestControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private BankAccountRepository bankAccountRepository;
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // createBankAccount tests
//    @Test
//    public void testCreateBankAccount_Success() throws Exception {
//        BankAccountRequestDTO request = new BankAccountRequestDTO();
//        request.setBalance(5000.0);
//        request.setRib("123456889");
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        // Generate token for testing
//        String token = jwtUtil.generateTestToken("testuser", Collections.singletonList("ROLE_USER"));
//
//        mockMvc.perform(post("/api/v1/accounts/create")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void testCreateBankAccount_CustomerNotFoundException() throws Exception {
//        BankAccountRequestDTO request = new BankAccountRequestDTO();
//        request.setBalance(5000.0);
//        request.setRib("123456889");
//
//        mockMvc.perform(post("/api/v1/accounts/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound());
//    }
//
//    // getAccountByRib tests
//    @Test
//    public void testGetAccountByRib_Success() throws Exception {
//        String rib = "123459789";
//        BankAccount bankAccount = new BankAccount();
//        bankAccount.setRib(rib);
//        bankAccount.setBalance(506.0);
//        bankAccountRepository.save(bankAccount);
//
//        // Generate a test token with appropriate roles
//        String token = jwtUtil.generateTestToken("testuser", Collections.singletonList("ROLE_ADMIN"));
//
//        mockMvc.perform(get("/api/v1/accounts/rib/{rib}", rib)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testGetAccountByRib_BankAccountNotFoundException() throws Exception {
//        String rib = "123456789";
//
//        // Generate a test token with appropriate roles
//        String token = jwtUtil.generateTestToken("testuser", Collections.singletonList("ROLE_ADMIN"));
//
//        mockMvc.perform(get("/api/v1/accounts/rib/{rib}", rib)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//}
