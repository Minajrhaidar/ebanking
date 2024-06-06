package ma.cigma.dto;

import lombok.Data;

@Data
public class BankAccountRequestDTO {
    private String rib;
    private Long customerId;
    private double balance;
}
