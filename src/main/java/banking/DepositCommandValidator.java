package banking;

public class DepositCommandValidator extends CommandValidator {

    public DepositCommandValidator(Bank bank) {
        super(bank);
    }

    public boolean validateDeposit(String[] commandArgs) {
        Account account = bank.retrieveAccount(commandArgs[1]);
        double amount;
        if (checkIdToDeposit(commandArgs[1]) && commandArgs.length == 3) {
            try {
                amount = Double.parseDouble(commandArgs[2]);
                return (account.checkDepositAmount(amount));
            } catch (NumberFormatException error) {
                return false;
            }
        }
        return false;
    }


    private boolean checkIdToDeposit(String idToDeposit) {
        return (bank.getAccounts().containsKey(idToDeposit));
    }
}
