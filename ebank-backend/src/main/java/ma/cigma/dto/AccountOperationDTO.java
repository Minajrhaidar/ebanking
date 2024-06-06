package ma.cigma.dto;

import lombok.Data;
import ma.cigma.entity.enums.OperationType;

@Data
public class AccountOperationDTO {
    private OperationType type;
    private double amount;
    private String description;

}
