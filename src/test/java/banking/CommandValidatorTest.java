package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;


    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void valid_create_command() {
        boolean actual = commandValidator.validate("create checking 12345678 2.2");
        assertTrue(actual);
    }

    @Test
    void create_in_uppercase_is_valid() {
        boolean actual = commandValidator.validate("CREATE savings 11223344 2.2");
        assertTrue(actual);
    }

    @Test
    void create_command_misspelled_is_invalid() {
        boolean actual = commandValidator.validate("crette cd 99887766 3.3 1000");
        assertFalse(actual);
    }

    @Test
    void create_command_word_with_other_characters_is_invalid() {
        boolean actual = commandValidator.validate("creat3() savings 11122233 5.55");
        assertFalse(actual);
    }
}
