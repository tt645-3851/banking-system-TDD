package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void create_command_is_empty() {
        boolean actual = commandValidator.validate(" ");
        assertFalse(actual);
    }

    @Test
    void valid_create_checking_command() {
        boolean actual = commandValidator.validate("create checking 12345678 2.22");
        assertTrue(actual);
    }

    @Test
    void valid_create_checking_command_with_one_account_in_bank() {
        bank.addAccount("checking", "11223344", 5.5, 0.00);
        boolean actual = commandValidator.validate("create checking 12345678 2.22");
        assertTrue(actual);
    }

    @Test
    void valid_create_checking_command_with_three_accounts_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create checking 99999991 5.5");
        assertTrue(actual);
    }

    @Test
    void valid_min_apr_when_creating_checking_account() {
        boolean actual = commandValidator.validate("create checking 22211122 0");
        assertTrue(actual);
    }

    @Test
    void valid_max_apr_when_creating_checking_account() {
        boolean actual = commandValidator.validate("create checking 22211122 10");
        assertTrue(actual);
    }

    @Test
    void negative_apr_in_create_command_is_invalid() {
        boolean actual = commandValidator.validate("create checking 00000001 -4.2");
        assertFalse(actual);
    }

    @Test
    void apr_more_than_10_percent_is_invalid() {
        boolean actual = commandValidator.validate("create checking 00000002 14.2");
        assertFalse(actual);
    }

    @Test
    void balance_for_checking_command_is_invalid() {
        boolean actual = commandValidator.validate("create checking 00000003 3.3 55.32");
        assertFalse(actual);
    }

/*
 * The next 3 test cases are checking for duplicate ID
 * by testing the account type. I will comment the account that
 * is tested and the account type it refers to.
 */

    @Test
    void duplicate_checking_account_id_is_invalid() {
        bank.addAccount("checking", "12345678", 2.22, 0.00); // This is checking account
        boolean actual = commandValidator.validate("create checking 12345678 2.22");
        assertFalse(actual);
    }

    @Test
    void duplicate_checking_account_id_is_invalid_from_cd_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500); // Tests cd account
        boolean actual = commandValidator.validate("create checking 11112233 5.5");
        assertFalse(actual);
    }

    @Test
    void duplicate_checking_account_id_is_invalid_from_savings_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00); // Tests savings account
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create checking 11112233 5.5");
        assertFalse(actual);
    }

    @Test
    void non_8_digit_checking_ID_is_invalid() {
        boolean actual = commandValidator.validate("create checking 11122 1.21");
        assertFalse(actual);
    }

    @Test
    void apr_is_not_numeric() {
        boolean actual = commandValidator.validate("create cd 99988877 we.324 5004");
        assertFalse(actual);
    }

    @Test
    void checking_id_with_other_characters_is_invalid() {
        boolean actual = commandValidator.validate("create checking dp99@tt6 8.8");
        assertFalse(actual);
    }

    @Test
    void missing_id_parameter_in_create_checking_command() {
        boolean actual = commandValidator.validate("create checking");
        assertFalse(actual);
    }

    @Test
    void missing_apr_parameter_in_create_checking_command() {
        boolean actual = commandValidator.validate("create checking 12345678");
        assertFalse(actual);
    }

    @Test
    void missing_account_type_in_create_checking_command() {
        boolean actual = commandValidator.validate("create 12345678 2.2");
        assertFalse(actual);
    }

    @Test
    void missing_more_than_one_parameter_in_checking_account() {
        boolean actual = commandValidator.validate("create 9.3");
        assertFalse(actual);
    }

/*
 * All create validation tests for savings account
 */

    @Test
    void valid_create_command_for_savings_account() {
        boolean actual = commandValidator.validate("create savings 23456789 4.4");
        assertTrue(actual);
    }

    @Test
    void valid_create_savings_command_with_one_account_in_bank() {
        bank.addAccount("savings", "11223344", 5.5, 0.00);
        boolean actual = commandValidator.validate("create savings 12345678 2.95");
        assertTrue(actual);
    }


    @Test
    void valid_create_savings_command_with_three_accounts_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create savings 99999991 5.5");
        assertTrue(actual);
    }

    @Test
    void valid_min_apr_when_creating_savings_account() {
        boolean actual = commandValidator.validate("create savings 22211122 0");
        assertTrue(actual);
    }

    @Test
    void valid_max_apr_when_savings_checking_account() {
        boolean actual = commandValidator.validate("create savings 22211122 10");
        assertTrue(actual);
    }

    @Test
    void negative_apr_in_create_savings_command_is_invalid() {
        boolean actual = commandValidator.validate("create savings 00000001 -4.2");
        assertFalse(actual);
    }

    @Test
    void apr_more_than_10_percent_in_savings_is_invalid() {
        boolean actual = commandValidator.validate("create savings 00000002 14.2");
        assertFalse(actual);
    }

    @Test
    void balance_for_savings_command_is_invalid() {
        boolean actual = commandValidator.validate("create savings 00000003 3.3 100.32");
        assertFalse(actual);
    }

    /*
    * Same scenario for duplicates, but for savings account
    */

    @Test
    void duplicate_savings_account_id_is_invalid() {
        bank.addAccount("savings", "12345678", 2.22, 0.00); // This is savings account
        boolean actual = commandValidator.validate("create savings 12345678 7.76");
        assertFalse(actual);
    }

    @Test
    void duplicate_savings_account_id_is_invalid_from_cd_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500); // Tests cd account
        boolean actual = commandValidator.validate("create savings 11112233 5.5");
        assertFalse(actual);
    }

    @Test
    void duplicate_savings_account_id_is_invalid_from_checking_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00); // Tests checking account
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create savings 11112233 5.5");
        assertFalse(actual);
    }

    @Test
    void non_8_digit_savings_ID_is_invalid() {
        boolean actual = commandValidator.validate("create savings 11122 1.21");
        assertFalse(actual);
    }

    @Test
    void savings_id_with_other_characters_is_invalid() {
        boolean actual = commandValidator.validate("create savings dp99@tt6 8.8");
        assertFalse(actual);
    }

    @Test
    void missing_id_parameter_in_create_savings_command() {
        boolean actual = commandValidator.validate("create savings 1.2");
        assertFalse(actual);
    }

    @Test
    void missing_apr_parameter_in_create_savings_command() {
        boolean actual = commandValidator.validate("create savings 11223344");
        assertFalse(actual);
    }

    @Test
    void missing_account_type_in_create_savings_command() {
        boolean actual = commandValidator.validate("create 11223344 2.2");
        assertFalse(actual);
    }

    @Test
    void missing_more_than_one_parameter_in_savings_account() {
        boolean actual = commandValidator.validate("savings 77788899");
        assertFalse(actual);
    }

    /*
     * All create validation tests for CD accounts
     */

    @Test
    void valid_create_cd_command() {
        boolean actual = commandValidator.validate("create cd 99887766 8.85 2050");
        assertTrue(actual);
    }

    @Test
    void valid_create_cd_command_with_one_account_in_bank() {
        bank.addAccount("savings", "11223344", 5.5, 0.00);
        boolean actual = commandValidator.validate("create cd 99887766 8.85 1004");
        assertTrue(actual);
    }

    @Test
    void valid_create_cd_command_with_three_accounts_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create cd 99887766 5.5 4000");
        assertTrue(actual);
    }

    @Test
    void valid_min_apr_when_creating_cd_account() {
        boolean actual = commandValidator.validate("create cd 99887766 0 4000");
        assertTrue(actual);
    }

    @Test
    void valid_max_apr_when_creating_cd_account() {
        boolean actual = commandValidator.validate("create cd 99887766 10 4000");
        assertTrue(actual);
    }

    @Test
    void negative_apr_is_invalid() {
        boolean actual = commandValidator.validate("create cd 99887766 -8.5 5009");
        assertFalse(actual);
    }

    @Test
    void apr_more_than_10_percent_is_invalid_for_cd_account() {
        boolean actual = commandValidator.validate("create cd 99887766 12.2 2000");
        assertFalse(actual);
    }

    @Test
    void valid_min_balance_when_creating_cd_account() {
        boolean actual = commandValidator.validate("create cd 99887766 5.43 1000");
        assertTrue(actual);
    }

    @Test
    void valid_max_balance_when_creating_cd_account() {
        boolean actual = commandValidator.validate("create cd 99887766 5.43 10000");
        assertTrue(actual);
    }

    @Test
    void balance_less_than_min_balance_is_invalid() {
        boolean actual = commandValidator.validate("create cd 99887766 5.43 450");
        assertFalse(actual);
    }

    @Test
    void balance_more_than_10000_is_invalid() {
        boolean actual = commandValidator.validate("create cd 99887766 5.43 10500");
        assertFalse(actual);
    }

    /*
     * Same scenario for checking duplicate ID in
     * bank, but now with CD account
     */

    @Test
    void duplicate_cd_account_id_is_invalid() {
        bank.addAccount("cd", "12345678", 2.22, 0.00); // This is cd account
        boolean actual = commandValidator.validate("create cd 12345678 2.22 3000");
        assertFalse(actual);
    }

    @Test
    void duplicate_cd_account_id_is_invalid_from_savings_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00);
        bank.addAccount("savings", "12345678", 9.5, 0.00); // Tests savings account
        bank.addAccount("cd", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create cd 12345678 5.5 3000");
        assertFalse(actual);
    }

    @Test
    void duplicate_cd_account_id_is_invalid_from_checking_account_in_bank() {
        bank.addAccount("checking", "99999992", 1.1, 0.00); // Tests checking account
        bank.addAccount("savings", "12345678", 9.5, 0.00);
        bank.addAccount("savings", "11112233", 4.4, 5500);
        boolean actual = commandValidator.validate("create cd 99999992 5.5 3000");
        assertFalse(actual);
    }

    @Test
    void non_8_digit_cd_account_id_is_invalid() {
        boolean actual = commandValidator.validate("create cd 555391 1.21 1004");
        assertFalse(actual);
    }

    @Test
    void cd_account_id_with_other_characters_is_invalid() {
        boolean actual = commandValidator.validate("create cd dp99@tt6 8.8 5004");
        assertFalse(actual);
    }

    @Test
    void cd_account_balance_is_not_numeric() {
        boolean actual = commandValidator.validate("create cd 99887766 8.8 34f4.45");
        assertFalse(actual);
    }

    @Test
    void missing_id_parameter_in_create_cd_command() {
        boolean actual = commandValidator.validate("create cd 5.4 2000");
        assertFalse(actual);
    }

    @Test
    void missing_apr_parameter_in_create_cd_command() {
        boolean actual = commandValidator.validate("create savings 11223344 5000");
        assertFalse(actual);
    }

    @Test
    void missing_account_type_in_create_cd_command() {
        boolean actual = commandValidator.validate("create 11223344 2.2 4002");
        assertFalse(actual);
    }

}
