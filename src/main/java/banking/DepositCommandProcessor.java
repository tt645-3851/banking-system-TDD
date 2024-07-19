package banking;

public class DepositCommandProcessor extends CommandProcessor{

    public DepositCommandProcessor(Bank bank) {
        super(bank);
    }

    public void depositAccount(String[] commandArgs) {
        double amount = Double.parseDouble(commandArgs[2]);
        bank.retrieveAccount(commandArgs[1]).depositCash(amount);
    }
}
