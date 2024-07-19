package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MasterControlTest {

    MasterControl masterControl;
    private ArrayList<String> input;
    private Bank bank;

    @BeforeEach
    void setup() {
        input = new ArrayList<>();
        bank = new Bank();
        masterControl = new MasterControl(new CommandValidator(bank),
                new CommandProcessor(bank), new CommandStorage(bank));
    }

    private void assertSingleCommand(String command, List<String> actual) {
        assertEquals(1, actual.size());
        assertEquals(command, actual.get(0));
    }

    @Test
    void typo_in_create_command_is_invalid() {
        input.add("creat checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("creat checking 12345678 1.0", actual);
    }

    @Test
    void typo_in_deposit_command_is_invalid() {
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("depositt 12345678 100", actual);
    }

    @Test
    void typo_in_withdraw_command_is_invalid() {
        input.add("wiithdra 12345678 400");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("wiithdra 12345678 400", actual);
    }

    @Test
    void two_typo_commands_both_invalid() {
        input.add("creat checking 12345678 1.0");
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("creat checking 12345678 1.0", actual.get(0));
        assertEquals("depositt 12345678 100", actual.get(1));
    }

    @Test
    void invalid_to_create_accounts_with_same_ID() {
        input.add("create checking 12345678 1.0");
        input.add("create checking 12345678 1.0");

        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
        assertEquals("create checking 12345678 1.0", actual.get(1));
    }

    @Test
    void check_accounts_that_have_zero_balance() {
        input.add("Create checking 12345678 1.0");
        input.add("Pass 1");

        List<String> actual = masterControl.start(input);

        assertEquals(0, actual.size());
        assertTrue(bank.accountExistsById("12345678"));
    }

    @Test
    void sample_list_of_inputs_part_1() {
        input.add("Create checking 12345678 1.12");
        input.add("Deposit 12345678 400");
        input.add("Pass 5");
        input.add("Create cd 09876543 4.23 5000.19");
        input.add("Pass 2");

        List<String> actual = masterControl.start(input);

        assertEquals(3, actual.size());
        assertEquals("Checking 12345678 402.62 1.12", actual.get(0));
        assertEquals("Deposit 12345678 400", actual.get(1));
        assertEquals("Cd 09876543 5142.94 4.23", actual.get(2));
    }

    @Test
    void sample_list_of_inputs_part_2_but_shows_commands_in_order() {
        input.add("Create checking 12345678 1.12");
        input.add("Deposit 12345678 400");
        input.add("Pass 5");
        input.add("Create savings 11223344 2.44");
        input.add("Deposit 11223344 3000");
        input.add("Deposit 11223344 2000");
        input.add("Withdraw 11223344 600");
        input.add("Withdraw 11223344 400.40");
        input.add("Pass 30");

        List<String> actual = masterControl.start(input);

        assertEquals(7, actual.size());
        assertEquals("Checking 12345678 413.27 1.12", actual.get(0));
        assertEquals("Deposit 12345678 400", actual.get(1));
        assertEquals("Savings 11223344 2125.66 2.44", actual.get(2));
        assertEquals("Deposit 11223344 2000", actual.get(3));
        assertEquals("Withdraw 11223344 600", actual.get(4));
        assertEquals("Deposit 11223344 3000", actual.get(5));
        assertEquals("Withdraw 11223344 400.40", actual.get(6));

    }

    @Test
    void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");
        input.add("Deposit 12345678 5000");
        input.add("creAte cHecKing 98765432 0.01");
        input.add("Deposit 98765432 300");
        input.add("Transfer 98765432 12345678 300");
        input.add("Pass 1");
        input.add("Create cd 23456789 1.2 2000");
        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Transfer 98765432 12345678 300", actual.get(2));
        assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
        assertEquals("Deposit 12345678 5000", actual.get(4));
    }

}
