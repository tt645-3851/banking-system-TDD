package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferCommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void missing_keyword_for_transfer_command() {
        assertFalse(commandValidator.validate("12345678 11223344 300.43"));
    }

    @Test
    void missing_id_to_transfer_from_account() {
        assertFalse(commandValidator.validate("transfer 11223344 300.43"));
    }

    @Test
    void missing_id_to_transfer_to_account() {
        assertFalse(commandValidator.validate("transfer 12345678 300.43"));
    }

    @Test
    void missing_amount_to_transfer_to_account() {
        assertFalse(commandValidator.validate("transfer 12345678 11223344"));
    }

    @Test
    void missing_more_than_one_parameter_in_transfer_command() {
        assertFalse(commandValidator.validate("transfer 12345678 43.23"));
    }

    @Test
    void empty_transfer_command_is_invalid() {
        assertFalse(commandValidator.validate(" "));
    }

    @Test
    void amount_to_transfer_is_not_numeric() {
        assertFalse(commandValidator.validate("transfer 12345678 11223344 23oip4.sdf"));
    }

    @Test
    void amount_to_transfer_should_not_be_negative() {
        assertFalse(commandValidator.validate("transfer 12345678 11223344 -502"));
    }

    @Test
    void id_for_transfer_is_not_numeric() {
        // Case for "from account"
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertFalse(commandValidator.validate("transfer 23uio4 11223344 150"));

        // Case for "to account"
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertFalse(commandValidator.validate("transfer 12345678 1122sjdkl3344 150"));

        // Case for both accounts
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertFalse(commandValidator.validate("transfer 23uio4 1122klj3344 150"));
    }

    @Test
    void valid_transfer_command() {
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertTrue(commandValidator.validate("transfer 12345678 11223344 300.43"));
    }

    @Test
    void valid_transfer_from_checking_to_checking() {
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("checking", "11223344", 5.44, 0.00);
        assertTrue(commandValidator.validate("transfer 12345678 11223344 500.21"));
    }

    @Test
    void valid_transfer_from_checking_to_savings() {
        bank.addAccount("checking", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertTrue(commandValidator.validate("transfer 12345678 11223344 300.32"));
    }

    @Test
    void valid_transfer_from_savings_to_savings() {
        bank.addAccount("savings", "12345678", 1.22, 0.00);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertTrue(commandValidator.validate("transfer 12345678 11223344 300.32"));
    }

    @Test
    void valid_transfer_from_savings_to_checking() {
        bank.addAccount("savings", "12345678", 1.22, 0.00);
        bank.addAccount("checking", "11223344", 5.44, 0.00);
        assertTrue(commandValidator.validate("transfer 12345678 11223344 300.32"));
    }

    @Test
    void transfer_from_cd_to_checking_is_invalid() {
        bank.addAccount("cd", "12345678", 1.22, 9000.12);
        bank.addAccount("checking", "11223344", 5.44, 0.00);
        assertFalse(commandValidator.validate("transfer 12345678 11223344 300.32"));
    }

    @Test
    void transfer_from_cd_to_savings_is_invalid() {
        bank.addAccount("cd", "12345678", 1.22, 6009.23);
        bank.addAccount("savings", "11223344", 5.44, 0.00);
        assertFalse(commandValidator.validate("transfer 12345678 11223344 300.32"));
    }

    @Test
    void transfer_from_checking_or_savings_to_cd_is_invalid() {
        bank.addAccount("savings", "12345678", 1.22, 6009.23);
        bank.addAccount("checking", "11223344", 5.44, 0.00);
        bank.addAccount("cd", "99887766", 1.22, 6009.23);
        assertFalse(commandValidator.validate("transfer 12345678 99887766 300.32"));
        assertFalse(commandValidator.validate("transfer 11223344 99887766 300.32"));
    }

}
