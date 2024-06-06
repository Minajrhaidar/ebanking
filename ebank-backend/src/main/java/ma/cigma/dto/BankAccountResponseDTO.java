package ma.cigma.dto;

import lombok.Data;

@Data
public class BankAccountResponseDTO {
    private Long id;
    private String rib;
    private double balance;
    private String status;
    private Long customerId;
}
