package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandStorage {

    private final Bank bank;
    private final List<String> invalidCommands;
    private final Map<String, List<String>> validCommands;

    public CommandStorage(Bank bank) {
        invalidCommands = new ArrayList<>();
        validCommands = new HashMap<>();
        this.bank = bank;
    }

    public List<String> getInvalidOutputs() {
        return invalidCommands;
    }

    public Map<String, List<String>> getValidCommands() {
        return validCommands;
    }

    public void addToInvalidCommandsList(String invalidCommand) {
        invalidCommands.add(invalidCommand);
    }

    public void addToValidCommandsList(String validCommand) {
        String[] commandArgs = validCommand.split(" ");

        if (commandArgs[0].equalsIgnoreCase("deposit") || commandArgs[0].equalsIgnoreCase("withdraw")) {
            mapToValidCommands(validCommands, commandArgs[1], validCommand);
        } else if (commandArgs[0].equalsIgnoreCase("transfer")) {
            mapToValidCommands(validCommands, commandArgs[1], validCommand);
            mapToValidCommands(validCommands, commandArgs[2], validCommand);
        }
    }

    private void mapToValidCommands(Map<String, List<String>> validCommandList, String ID, String validCommand) {
        if (validCommandList.get(ID) != null) {
            validCommandList.get(ID).add(validCommand);
        } else if (validCommandList.get(ID) == null) {
            validCommandList.put(ID, new ArrayList<>());
            validCommandList.get(ID).add(validCommand);
        }
    }

    public List<String> getOutput() {
        List<String> output = new ArrayList<>();
        for (String id : bank.getAccountList()) {
            Account account = bank.getAccounts().get(id);
            output.add(account.getCurrentStatus(account));
            if (validCommands.get(id) != null) {
                output.addAll(validCommands.get(id));
            }
        }
        output.addAll(invalidCommands);
        return output;
    }
}
