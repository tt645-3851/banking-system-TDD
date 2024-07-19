package banking;

public class CreateCommandProcessor extends CommandProcessor {

    public CreateCommandProcessor(Bank bank) {
        super(bank);
    }

    public void createAccount(String[] commandArgs) {
        if (commandArgs[1].equalsIgnoreCase("checking")) {
            bank.addAccount(commandArgs[1], commandArgs[2], Double.parseDouble(commandArgs[3]), 0.00);
        } else if (commandArgs[1].equalsIgnoreCase("savings")) {
            bank.addAccount(commandArgs[1], commandArgs[2], Double.parseDouble(commandArgs[3]), 0.00);
        } else {
            bank.addAccount(commandArgs[1], commandArgs[2], Double.parseDouble(commandArgs[3]), Double.parseDouble(commandArgs[4]));
        }
    }



}
