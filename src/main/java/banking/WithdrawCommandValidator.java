package banking;

public class WithdrawCommandValidator extends CommandValidator{

    public WithdrawCommandValidator (Bank bank) {
        super(bank);
    }


    public boolean validateWithdraw(String[] commandArgs) {
        Account account = bank.retrieveAccount(commandArgs[1]);
        double amount;
        if (checkIdToWithdraw(commandArgs[1]) && commandArgs.length == 3) {
            try {
                amount = Double.parseDouble(commandArgs[2]);
            } catch (NumberFormatException error) {
                return false;
            }
            return account.checkWithdrawAmount(amount);
        }
        return false;
    }

    private boolean checkIdToWithdraw(String commandArg) {
        return bank.getAccounts().containsKey(commandArg);
    }
}
