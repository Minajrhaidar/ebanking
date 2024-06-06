package ma.cigma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {
    private Long id;

    @NotNull(message = "Veuillez entrer le nom complet du client svp!")
    private String name;

    @NotNull(message = "Veuillez entrer le numéro d'identité!")
    private String cin;

    @Email
    @NotNull(message = "Veuillez entrer un email valide!")
    private String email;

    @NotNull(message = "Veuillez entrer la date d'anniversaire!")
    private Date birthday;

    @NotNull(message = "Veuillez entrer l'adresse postale!")
    private String postalAddress;
}