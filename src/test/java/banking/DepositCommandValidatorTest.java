package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DepositCommandValidatorTest {

    public static final String CHECKING_ID = "12345678";
    public static final String SAVINGS_ID = "22334455";
    private CommandValidator commandValidator;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void deposit_command_is_empty() {
        boolean actual = commandValidator.validate(" ");
        assertFalse(actual);
    }

    @Test
    void missing_id_in_deposit_command() {
        boolean actual = commandValidator.validate("deposit 540");
        assertFalse(actual);
    }

    @Test
    void missing_amount_to_deposit() {
        boolean actual = commandValidator.validate("deposit 34567890");
        assertFalse(actual);
    }

    @Test
    void missing_deposit_keyword_in_command() {
        boolean actual = commandValidator.validate("11229933 900");
        assertFalse(actual);
    }

    @Test
    void missing_more_than_one_parameter_in_deposit_command() {
        boolean actual = commandValidator.validate("48333111");
        assertFalse(actual);
    }

    @Test
    void misspelled_deposit_keyword_is_invalid() {
        boolean actual = commandValidator.validate("depmit 55544439 102");
        assertFalse(actual);
    }

    @Test
    void id_with_other_characters_is_invalid() {
        bank.addAccount("savings", "55511123", 8.77, 0);
        boolean actual = commandValidator.validate("deposit 555wer4%39 102");
        assertFalse(actual);
    }

    @Test
    void amount_is_not_numeric() {
        boolean actual = commandValidator.validate("deposit 55544439 wer980");
        assertFalse(actual);
    }

    @Test
    void depositing_in_bank_with_no_accounts() {
        boolean actual = commandValidator.validate("deposit 12345678 500");
        assertFalse(actual);
    }

    @Test
    void deposit_keyword_in_uppercase_and_lowercase_is_valid() {
        bank.addAccount("checking", "99988866", 6.66, 0);
        boolean actual = commandValidator.validate("dEPOsiT 99988866 355");
        assertTrue(actual);
    }

    @Test
    void valid_deposit_command() {
        bank.addAccount("savings", "99900021", 4.24, 0);
        boolean actual = commandValidator.validate("deposit 99900021 100");
        assertTrue(actual);
    }

    @Test
    void valid_deposit_amount_can_be_zero() {
        bank.addAccount("savings", "55004433", 3.50, 0);
        boolean actual = commandValidator.validate("deposit 55004433 0");
        assertTrue(actual);
    }

    @Test
    void deposit_negative_amount_in_account_is_invalid() {
        bank.addAccount("checking", "99002211", 3.50, 0);
        boolean actual = commandValidator.validate("deposit 99002211 -444.23");
        assertFalse(actual);
    }

    @Test
    void deposit_negative_amount_in_savings_account_is_invalid() {
        bank.addAccount("savings", "09876543", 7.77, 0);
        boolean actual = commandValidator.validate("deposit 09876543 -100.23");
        assertFalse(actual);
    }

    /*
     *  All validation deposit test cases for checking account
     */

    @Test
    void valid_deposit_checking_command() {
        bank.addAccount("checking", CHECKING_ID, 3.50, 0);
        boolean actual = commandValidator.validate("deposit 12345678 100");
        assertTrue(actual);
    }

    @Test
    void existing_checking_account_can_be_deposited() {
        bank.addAccount("checking", CHECKING_ID, 2.22, 0.00);
        boolean actual = commandValidator.validate("deposit 12345678 350");
        assertTrue(actual);
    }

    @Test
    void valid_max_deposit_for_checking_is_1000() {
        bank.addAccount("checking", CHECKING_ID, 3.50, 0);
        boolean actual = commandValidator.validate("deposit 12345678 1000");
        assertTrue(actual);
    }

    @Test
    void deposit_over_checking_limit_is_invalid() {
        bank.addAccount("checking", CHECKING_ID, 3.50, 0);
        boolean actual = commandValidator.validate("deposit 12345678 1050");
        assertFalse(actual);
    }

    /*
     *  All validation deposit test cases for savings account
     */

    @Test
    void valid_deposit_savings_command() {
        bank.addAccount("savings", SAVINGS_ID, 5.55, 0);
        boolean actual = commandValidator.validate("deposit 22334455 100");
        assertTrue(actual);
    }

    @Test
    void existing_savings_account_can_be_deposited() {
        bank.addAccount("savings", SAVINGS_ID, 5.55, 0);
        boolean actual = commandValidator.validate("deposit 22334455 650");
        assertTrue(actual);
    }

    @Test
    void valid_max_deposit_for_savings_is_2500() {
        bank.addAccount("savings", SAVINGS_ID, 3.50, 0);
        boolean actual = commandValidator.validate("deposit 22334455 2500");
        assertTrue(actual);
    }

    @Test
    void deposit_over_savings_limit_is_invalid() {
        bank.addAccount("savings", SAVINGS_ID, 3.50, 0);
        boolean actual = commandValidator.validate("deposit 22334455 4000");
        assertFalse(actual);
    }

    @Test
    void cd_account_cannot_be_deposited() {
        bank.addAccount("cd", "99880011", 3.50, 5000);
        boolean actual = commandValidator.validate("deposit 99880011 650");
        assertFalse(actual);

    }

}
