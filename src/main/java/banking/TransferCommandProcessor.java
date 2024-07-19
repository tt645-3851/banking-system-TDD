package banking;

public class TransferCommandProcessor extends CommandProcessor {

    public TransferCommandProcessor(Bank bank) {
        super(bank);
    }

    public void executeTransfer(String[] commandArgs) {
        Account fromAccount = bank.retrieveAccount(commandArgs[1]);
        Account toAccount = bank.retrieveAccount(commandArgs[2]);
        double amount = Double.parseDouble(commandArgs[3]);

        if (amount >= fromAccount.getBalance()) {
            amount = fromAccount.getBalance();
        }
        bank.transferAmountToID(fromAccount, toAccount, amount);
    }
}
