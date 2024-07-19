package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PassCommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void missing_keyword_for_pass_command() {
        assertFalse(commandValidator.validate("234"));
    }

    @Test
    void misspelled_keyword_for_pass_command() {
        assertFalse(commandValidator.validate("Psdklfj 234"));
    }

    @Test
    void missing_parameter_for_months_to_pass() {
        assertFalse(commandValidator.validate("pass"));
    }

    @Test
    void months_to_pass_is_not_numeric() {
        assertFalse(commandValidator.validate("pass 32o4"));
    }

    @Test
    void valid_pass_command() {
        boolean actual = commandValidator.validate("pass 1");
        assertTrue(actual);
    }

    @Test
    void pass_10_months_is_valid() {
        boolean actual = commandValidator.validate("pass 10");
        assertTrue(actual);
    }

    @Test
    void maximum_months_passed_is_valid() {
        boolean actual = commandValidator.validate("pass 60");
        assertTrue(actual);
    }

    @Test
    void pass_zero_months_is_invalid() {
        boolean actual = commandValidator.validate("pass 0");
        assertFalse(actual);
    }

    @Test
    void pass_61_months_is_invalid() {
        boolean actual = commandValidator.validate("pass 61");
        assertFalse(actual);
    }

    @Test
    void passing_in_negative_months_is_invalid() {
        boolean actual = commandValidator.validate("pass -12");
        assertFalse(actual);
    }

}
