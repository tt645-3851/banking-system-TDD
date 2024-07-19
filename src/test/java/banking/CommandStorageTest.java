package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandStorageTest {

    CommandStorage commandStorage;
    Bank bank;
    String commandInput;

    @BeforeEach
    void setup() {
        bank = new Bank();
        commandStorage = new CommandStorage(bank);
    }

    @Test
    void store_invalid_create_command() {
        commandInput = "create chekin 125436 4.dfgkl";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_create_command_with_misspelled_keyword() {
        commandInput = "cert checking 99999999 8.78";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_create_command_with_misspelled_account_type() {
        commandInput = "create check 99999999 8.78";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_create_command_with_invalid_id() {
        commandInput = "create checking 12uio3 8.78";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_create_command_with_invalid_apr() {
        commandInput = "create checking 99999999 8.wekjlr";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_create_cd_command_with_invalid_balance() {
        commandInput = "create cd 99999999 8.78 2op34";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_deposit_command() {
        commandInput = "deposit 222299sdf99 3.2k";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_deposit_command_with_misspelled_keyword() {
        commandInput = "d30psit 11122233 493.11";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_deposit_command_with_invalid_id() {
        commandInput = "deposit eoript4359 3.33";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_deposit_command_with_invalid_apr() {
        commandInput = "deposit 11223344 23gr.34";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_command_with_one_parameter_missing() {
        commandInput = "deposit 3.2k";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_command_with_more_than_four_parameters_for_checking_and_savings() {
        commandInput = "create checking 222299sdf99 3.2k null ";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_invalid_command_with_more_than_two_parameters_missing() {
        commandInput = "create cd";
        commandStorage.addToInvalidCommandsList(commandInput);
        assertTrue(commandStorage.getInvalidOutputs().contains(commandInput));
    }

    @Test
    void store_multiple_invalid_commands() {
        commandStorage.addToInvalidCommandsList("depsoit ..");
        commandStorage.addToInvalidCommandsList("create 24sdfv 345.43");
        assertEquals(2, commandStorage.getInvalidOutputs().size());
    }

    @Test
    void store_invalid_command_with_all_parameters_missing() {
        commandStorage.addToInvalidCommandsList(" ");
        assertTrue(commandStorage.getInvalidOutputs().contains(" "));
    }

    @Test
    void store_valid_command_for_command_output() {
        bank.addAccount("checking", "12345678", 0.6, 0.00);
        commandStorage.addToValidCommandsList("Deposit 12345678 850");
        assertEquals("Deposit 12345678 850", commandStorage.getValidCommands().get("12345678").get(0));
    }

    @Test
    void store_multiple_valid_commands_for_command_output() {
        bank.addAccount("checking", "12345678", 0.6, 0.00);
        commandStorage.addToValidCommandsList("Deposit 12345678 850");
        commandStorage.addToValidCommandsList("Withdraw 12345678 200");
        assertEquals("Deposit 12345678 850", commandStorage.getValidCommands().get("12345678").get(0));
        assertEquals("Withdraw 12345678 200", commandStorage.getValidCommands().get("12345678").get(1));
    }

    @Test
    void store_valid_transfer_command_for_different_accounts() {
        bank.addAccount("checking", "12345678", 0.6, 0.00);
        bank.addAccount("savings", "11223344", 9.10, 0.00);
        commandStorage.addToValidCommandsList("Deposit 12345678 850");
        commandStorage.addToValidCommandsList("Withdraw 12345678 200");
        commandStorage.addToValidCommandsList("Transfer 12345678 11223344 300");
        assertEquals("Deposit 12345678 850", commandStorage.getValidCommands().get("12345678").get(0));
        assertEquals("Withdraw 12345678 200", commandStorage.getValidCommands().get("12345678").get(1));
        assertEquals("Transfer 12345678 11223344 300", commandStorage.getValidCommands().get("12345678").get(2));
        assertEquals("Transfer 12345678 11223344 300", commandStorage.getValidCommands().get("11223344").get(0));
    }





}
