package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {

    private Bank bank;
    private CommandProcessor commandProcessor;

    @BeforeEach
    public void CommandProcessor(){
        bank = new Bank();
        commandProcessor = new CommandProcessor(bank);
    }

    @Test
    void create_valid_checking_account() {
        commandProcessor.process("create checking 12345678 9.55");
        assertTrue( bank.getAccounts().containsKey("12345678"));
        assertEquals(9.55, bank.retrieveAccount("12345678").getAPR());
    }

    @Test
    void create_valid_savings_account() {
        commandProcessor.process("create savings 87654321 3.35");
        assertTrue(bank.getAccounts().containsKey("87654321"));
        assertEquals(3.35, bank.retrieveAccount("87654321").getAPR());
    }

    @Test
    void create_valid_cd_account() {
        commandProcessor.process("create cd 11223344 9.22 5003.43");
        assertTrue(bank.getAccounts().containsKey("11223344"));
        assertEquals(9.22, bank.retrieveAccount("11223344").getAPR());
        assertEquals(5003.43, bank.retrieveAccount("11223344").getBalance());
    }

    @Test
    void create_account_with_one_account_in_bank() {
        bank.addAccount("savings", "87654321", 2.21, 0.00);
        commandProcessor.process("create checking 12345678 9.55");
        assertEquals(2, bank.getAccounts().size());
        assertTrue( bank.getAccounts().containsKey("12345678"));
    }

    @Test
    void create_account_with_multiple_accounts_in_bank() {
        bank.addAccount("savings", "87654321", 2.21, 0.00);
        bank.addAccount("checking", "00000001", 5.54, 0.00);
        bank.addAccount("savings", "22333444", 8.87, 0.00);
        commandProcessor.process("create cd 12345678 9.55 1000.23");
        assertEquals(4, bank.getAccounts().size());
        assertTrue( bank.getAccounts().containsKey("12345678"));
    }

    @Test
    void deposit_amount_into_account_with_no_balance() {
        commandProcessor.process("create checking 12345678 4.33");
        commandProcessor.process("deposit 12345678 500");
        assertEquals(500, bank.retrieveAccount("12345678").getBalance());
    }

    @Test
    void deposit_amount_into_account_using_id() {
        commandProcessor.process("create savings 11223344 2.25");
        bank.retrieveAccount("11223344").depositCash(550);
        commandProcessor.process("deposit 11223344 550.19");
        assertEquals(1100.19, bank.retrieveAccount("11223344").getBalance());
    }

    @Test
    void deposit_amount_into_account_twice() {
        bank.addAccount("savings", "11223344", 2.11, 0.00);
        commandProcessor.process("deposit 11223344 550.00");
        assertEquals(550, bank.retrieveAccount("11223344").getBalance());
        commandProcessor.process("deposit 11223344 550.19");
        assertEquals(1100.19, bank.retrieveAccount("11223344").getBalance());
    }

    @Test
    void deposit_amount_into_correct_account_with_multiple_accounts_in_bank() {
        bank.addAccount("savings", "87654321", 2.21, 0.00);
        bank.addAccount("checking", "00000001", 5.54, 0.00);
        bank.addAccount("savings", "22333444", 8.87, 0.00);
        commandProcessor.process("deposit 22333444 600.23");
        assertEquals(600.23, bank.retrieveAccount("22333444").getBalance());
    }

    @Test
    void withdraw_amount_in_account() {
        bank.addAccount("checking", "00000001", 5.54, 0.00);
        Account account = bank.retrieveAccount("00000001");
        account.depositCash(500);
        commandProcessor.process("withdraw 00000001 400");
        assertEquals(100, account.getBalance());
    }

    @Test
    void withdraw_savings_account_and_uses_withdrawal_limit() {
        bank.addAccount("savings", "11223344", 2.22, 0.00);
        Account account = bank.retrieveAccount("11223344");
        account.depositCash(800.45);
        commandProcessor.process("withdraw 11223344 500.20");
        assertEquals(300.25, account.getBalance(), 0.01);
    }

    @Test
    void withdraw_cd_account_with_exact_balance() {
        bank.addAccount("cd", "09876543", 8.53, 8000.21);
        Account account = bank.retrieveAccount("09876543");
        account.months = 12;
        commandProcessor.process("withdraw 09876543 8000.21");
        assertEquals(0, account.getBalance());
    }

    @Test
    void withdraw_amount_in_cd_account_past_initial_balance() {
        bank.addAccount("cd", "09876543", 8.53, 3000.12);
        Account account = bank.retrieveAccount("09876543");
        account.months = 12;
        commandProcessor.process("withdraw 09876543 5000.23");
        assertEquals(0, account.getBalance());

    }

    @Test
    void withdraw_amount_in_correct_account() {
        bank.addAccount("savings", "87654321", 2.21, 0.00);
        bank.addAccount("checking", "00000001", 5.54, 0.00);
        bank.addAccount("cd", "22333444", 8.87, 1000.33);
        bank.retrieveAccount("87654321").depositCash(550.12);
        bank.retrieveAccount("00000001").depositCash(230.11);
        commandProcessor.process("withdraw 87654321 300");
        assertEquals(250.12, bank.retrieveAccount("87654321").getBalance());
    }

    @Test
    void withdraw_from_separate_accounts() {
        bank.addAccount("savings", "87654321", 2.21, 0.00);
        bank.addAccount("checking", "00000001", 5.54, 0.00);
        bank.addAccount("cd", "22333444", 8.87, 1000.33);
        bank.retrieveAccount("22333444").months = 12;
        bank.retrieveAccount("87654321").depositCash(550.12);
        bank.retrieveAccount("00000001").depositCash(230.11);
        commandProcessor.process("withdraw 87654321 300");
        commandProcessor.process("withdraw 22333444 3000.21");
        assertEquals(250.12, bank.retrieveAccount("87654321").getBalance());
        assertEquals(0, bank.retrieveAccount("22333444").getBalance());
    }

    @Test
    void withdraw_from_checking_account_twice() {
        bank.addAccount("checking", "12345678", 8.88, 0.00);
        bank.retrieveAccount("12345678").depositCash(1000);

        commandProcessor.process("withdraw 12345678 250");
        assertEquals(750, bank.retrieveAccount("12345678").getBalance());

        commandProcessor.process("withdraw 12345678 250.11");
        assertEquals(499.89, bank.retrieveAccount("12345678").getBalance());
    }

    @Test
    void withdraw_from_account_with_exact_amount() {
        bank.addAccount("checking", "12345678", 8.88, 0.00);
        bank.retrieveAccount("12345678").depositCash(200);

        commandProcessor.process("withdraw 12345678 200");
        assertEquals(0.00, bank.retrieveAccount("12345678").getBalance());
    }

    @Test
    void transfer_from_checking_to_checking() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("checking", "00000001", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(500);
        bank.retrieveAccount("00000001").depositCash(430);

        commandProcessor.process("transfer 12345678 00000001 200");
        assertEquals(300, bank.retrieveAccount("12345678").getBalance());
        assertEquals(630, bank.retrieveAccount("00000001").getBalance());
    }

    @Test
    void transfer_from_checking_to_savings() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("savings", "00000001", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(500);
        bank.retrieveAccount("00000001").depositCash(430);

        commandProcessor.process("transfer 12345678 00000001 400");
        assertEquals(100, bank.retrieveAccount("12345678").getBalance(), 0.01);
        assertEquals(830, bank.retrieveAccount("00000001").getBalance(), 0.01);
    }

    @Test
    void transfer_from_savings_to_savings() {
        bank.addAccount("savings", "12345678", 3.32, 0.00);
        bank.addAccount("savings", "00000001", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(1000);
        bank.retrieveAccount("00000001").depositCash(720.12);

        commandProcessor.process("transfer 12345678 00000001 200");
        assertEquals(800, bank.retrieveAccount("12345678").getBalance());
        assertEquals(920.12, bank.retrieveAccount("00000001").getBalance());
    }

    @Test
    void transfer_from_savings_to_checking() {
        bank.addAccount("savings", "12345678", 3.32, 0.00);
        bank.addAccount("checking", "00000001", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(870.99);
        bank.retrieveAccount("00000001").depositCash(430);

        commandProcessor.process("transfer 12345678 00000001 200");
        assertEquals(670.99, bank.retrieveAccount("12345678").getBalance());
        assertEquals(630, bank.retrieveAccount("00000001").getBalance());
    }

    @Test
    void transfer_zero_from_account_to_another() {
        bank.addAccount("savings", "12345678", 3.32, 0.00);
        bank.addAccount("checking", "00000001", 3.32, 0.00);
        bank.retrieveAccount("00000001").depositCash(430);

        commandProcessor.process("transfer 12345678 00000001 0.00");
        assertEquals(0.00, bank.retrieveAccount("12345678").getBalance());
        assertEquals(430, bank.retrieveAccount("00000001").getBalance());

    }

    @Test
    void transfer_more_than_first_account() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("checking", "00000001", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(400);
        bank.retrieveAccount("00000001").depositCash(430);

        commandProcessor.process("transfer 12345678 00000001 400");
        assertEquals(0, bank.retrieveAccount("12345678").getBalance());
        assertEquals(830, bank.retrieveAccount("00000001").getBalance());
    }

    @Test
    void pass_one_month_for_checking_account_in_bank_and_calculate_apr() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        Account account = bank.retrieveAccount("12345678");
        account.depositCash(300);
        commandProcessor.process("pass 1");
        assertEquals(1, account.getMonths());
        assertEquals(300.83, account.getBalance());
    }

    @Test
    void pass_one_month_for_savings_account_in_bank_and_calculate_apr() {
        bank.addAccount("savings", "11223344", 8.87, 0.00);
        Account account = bank.retrieveAccount("11223344");
        account.depositCash(550);
        commandProcessor.process("pass 1");
        assertEquals(1, account.getMonths());
        assertEquals(554.06, account.getBalance(), 0.01);
    }

    @Test
    void pass_one_month_for_cd_account_in_bank_and_calculate_apr() {
        bank.addAccount("cd", "09876543", 2.21, 2000);
        Account account = bank.retrieveAccount("09876543");
        commandProcessor.process("pass 1");
        assertEquals(1, account.getMonths());
        assertEquals(2014.77, account.getBalance(), 0.01);
    }

    @Test
    void pass_multiple_months_in_an_account() {
        bank.addAccount("cd", "09876543", 8.87, 8000);
        Account account = bank.retrieveAccount("09876543");
        commandProcessor.process("pass 5");
        assertEquals(5, account.getMonths());
        assertEquals(9269.51, account.getBalance(), 0.01);
    }

    @Test
    void pass_multiple_months_in_multiple_accounts() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("savings", "11223344", 8.87, 0.00);
        bank.addAccount("cd", "09876543", 8.87, 8000);
        Account account1 = bank.retrieveAccount("12345678");
        Account account2 = bank.retrieveAccount("11223344");
        Account account3 = bank.retrieveAccount("09876543");
        account1.depositCash(350);
        account2.depositCash(700);
        commandProcessor.process("pass 3");

        assertEquals(3, account1.months);
        assertEquals(3, account2.months);
        assertEquals(3, account3.months);

        assertEquals(352.91, account1.getBalance(), 0.01);
        assertEquals(715.63, account2.getBalance(), 0.01);
        assertEquals(8739.17, account3.getBalance(), 0.01);

    }

    @Test
    void close_accounts_with_zero_balance_and_calculates_apr_for_the_rest_of_available_accounts() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("savings", "11223344", 8.87, 0.00);
        bank.addAccount("cd", "09876543", 8.87, 8000);
        commandProcessor.process("pass 1");

        assertEquals(1, bank.getAccounts().size());
        assertEquals(8239.16, bank.retrieveAccount("09876543").getBalance(), 0.01);
    }

    @Test
    void reduce_any_account_balance_less_than_100() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.addAccount("savings", "11223344", 8.87, 0.00);
        bank.retrieveAccount("12345678").depositCash(600);
        bank.retrieveAccount("11223344").depositCash(90);

        commandProcessor.process("pass 1");
        assertEquals(65.48, bank.retrieveAccount("11223344").getBalance(), 0.01);
    }

    @Test
    void pass_one_month_with_account_balance_at_100() {
        bank.addAccount("checking", "12345678", 3.32, 0.00);
        bank.retrieveAccount("12345678").depositCash(100);

        commandProcessor.process("pass 1");
        assertEquals(100.27, bank.retrieveAccount("12345678").getBalance(), 0.01);

    }

}
