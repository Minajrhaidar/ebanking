package ma.cigma.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
