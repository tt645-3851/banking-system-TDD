package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WithdrawCommandValidatorTest {

    private CommandValidator commandValidator;
    private Bank bank;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void missing_keyword_for_withdraw_command() {
        assertFalse(commandValidator.validate("12456789 100.32"));
    }

    @Test
    void misspelled_withdraw_keyword_is_invalid() {
        boolean actual = commandValidator.validate("withD92 12345678 350.21");
        assertFalse(actual);
    }

    @Test
    void missing_id_parameter_in_command() {
        assertFalse(commandValidator.validate("withdraw 2.34"));
    }

    @Test
    void missing_amount_to_withdraw_from_account() {
        assertFalse(commandValidator.validate("withdraw 99880000"));
    }

    @Test
    void missing_more_than_one_parameter_in_command() {
        assertFalse(commandValidator.validate("withdraw"));
    }

    @Test
    void empty_withdraw_command_is_invalid() {
        assertFalse(commandValidator.validate(" "));
    }

    @Test
    void valid_withdraw_command() {
        bank.addAccount("checking", "12345678", 4.33, 0.00);
        bank.retrieveAccount("12345678").depositCash(2050);
        boolean actual = commandValidator.validate("withdraw 12345678 320.00");
        assertTrue(actual);
    }

    @Test
    void valid_withdraw_can_be_zero_for_checking_or_savings_account() {
        bank.addAccount("savings", "11223344", 2.25, 0.00);
        bank.addAccount("checking", "12345678", 6.50, 0.00);
        bank.retrieveAccount("11223344").depositCash(400);
        bank.retrieveAccount("12345678").depositCash(250);
        assertTrue(commandValidator.validate("withdraw 11223344 0"));
        assertTrue(commandValidator.validate("withdraw 12345678 0"));
    }

    @Test
    void withdrawing_more_than_account_is_valid() {
        bank.addAccount("savings", "12345678", 0.34, 0.00);
        Account account = bank.retrieveAccount("12345678");
        account.depositCash(850);
        assertTrue(commandValidator.validate("withdraw 12345678 900"));
    }

    @Test
    void valid_max_withdraw_for_checking_account() {
        bank.addAccount("checking", "12345678", 6.50, 0.00);
        bank.retrieveAccount("12345678").depositCash(1000);
        boolean actual = commandValidator.validate("withdraw 12345678 400");
        assertTrue(actual);
    }

    @Test
    void valid_max_withdraw_for_savings_account() {
        bank.addAccount("savings", "11223344", 2.25, 0.00);
        bank.retrieveAccount("11223344").depositCash(1000);
        boolean actual = commandValidator.validate("withdraw 11223344 0");
        assertTrue(actual);
    }

    @Test
    void valid_withdraw_for_savings_account_uses_withdrawal_limit() {
        bank.addAccount("savings", "11223344", 2.25, 0.00);
        Account account = bank.retrieveAccount("11223344");
        account.depositCash(1000);
        boolean actual = commandValidator.validate("withdraw 11223344 0");
        assertTrue(actual);
    }

    @Test
    void withdrawing_savings_account_twice_in_one_month_is_invalid() {
        bank.addAccount("savings", "11223344", 2.25, 0.00);
        Account account = bank.retrieveAccount("11223344");
        account.depositCash(550);
        assertTrue(commandValidator.validate("withdraw 11223344 0"));
        account.withdrawCash(320);
        assertFalse(commandValidator.validate("withdraw 11223344 451"));
    }

    @Test
    void valid_cd_account_withdrawal() {
        bank.addAccount("cd", "99887766", 5.34, 5500.55);
        Account account = bank.retrieveAccount("99887766");
        account.months = 12;
        assertTrue(commandValidator.validate("withdraw 99887766 5500.55"));
    }

    @Test
    void amount_to_withdraw_is_the_same_as_account_balance() {
        bank.addAccount("savings", "11223344", 5.34, 0.00);
        Account account = bank.retrieveAccount("11223344");
        account.depositCash(700);
        assertTrue(commandValidator.validate("withdraw 11223344 700"));

    }

    @Test
    void valid_withdrawal_amount_greater_than_cd_account_balance() {
        bank.addAccount("cd", "99887766", 5.34, 5500.55);
        Account account = bank.retrieveAccount("99887766");
        account.months = 12;
        assertTrue(commandValidator.validate("withdraw 99887766 10000.25"));
    }

    @Test
    void amount_less_than_cd_balance_is_invalid() {
        bank.addAccount("cd", "99887766", 2.23, 3050.23);
        Account account = bank.retrieveAccount("99887766");
        account.months = 12;
        assertFalse(commandValidator.validate("withdraw 99887766 550.12"));
    }

    @Test
    void months_passed_less_than_12_months_is_invalid() {
        bank.addAccount("cd", "99887766", 2.23, 3050.23);
        Account account = bank.retrieveAccount("99887766");
        account.months = 12;
        assertFalse(commandValidator.validate("withdraw 99887766 550.12"));
    }

    @Test
    void invalid_amount_to_withdraw_and_12_months_not_passed() {
        bank.addAccount("cd", "99887766", 2.23, 3050.23);
        Account account = bank.retrieveAccount("99887766");
        account.months = 3;
        assertFalse(commandValidator.validate("withdraw 99887766 3000.12"));
    }

    @Test
    void account_to_withdraw_does_not_exist() {
        bank.addAccount("checking", "12345678", 9.11, 0.00);
        boolean actual = commandValidator.validate("withdraw 12234567 220.35");
        assertFalse(actual);
    }

    @Test
    void account_id_to_withdraw_is_not_numeric() {
        assertFalse(commandValidator.validate("withdraw 23iop4dvfnl 500.23"));
    }

    @Test
    void amount_to_withdraw_is_not_numeric() {
        bank.addAccount("savings", "11223344", 2.21, 0.00);
        bank.retrieveAccount("11223344").depositCash(550.33);
        boolean actual = commandValidator.validate("withdraw 11223344 e98rw.sdf");
        assertFalse(actual);
    }

    @Test
    void amount_to_withdraw_cannot_be_negative() {
        bank.addAccount("savings", "11223344", 2.21, 0.00);
        bank.retrieveAccount("11223344").depositCash(550.33);
        boolean actual = commandValidator.validate("withdraw 11223344 -550.00");
        assertFalse(actual);
    }

}
