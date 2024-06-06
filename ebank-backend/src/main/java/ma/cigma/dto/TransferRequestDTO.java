package ma.cigma.dto;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String sourceRib;
    private String destinationRib;
    private double amount;
    private String description;
}