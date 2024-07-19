package banking;

public class PassCommandProcessor extends CommandProcessor{

    public PassCommandProcessor(Bank bank) {
        super(bank);
    }

    public void executePass(String[] commandArgs) {
        int months = Integer.parseInt(commandArgs[1]);
        bank.passTime(months);
    }
}
