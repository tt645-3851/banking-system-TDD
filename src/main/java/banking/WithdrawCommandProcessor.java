package banking;

public class WithdrawCommandProcessor extends CommandProcessor{

    public WithdrawCommandProcessor(Bank bank) {
        super(bank);
    }


    public void withdrawAccount(String[] commandArgs) {
        double amount = Double.parseDouble(commandArgs[2]);
        bank.retrieveAccount(commandArgs[1]).withdrawCash(amount);
    }
}
