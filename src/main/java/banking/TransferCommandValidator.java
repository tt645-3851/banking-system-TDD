package banking;

public class TransferCommandValidator extends CommandValidator {

    public TransferCommandValidator(Bank bank) {
        super(bank);
    }

    public boolean validateTransfer(String[] commandArgs) {
        if (commandArgs.length == 4 && validAmountToTransfer(commandArgs[3]) && idIsValid(commandArgs[1], commandArgs[2])) {
            Account fromAccount = bank.retrieveAccount(commandArgs[1]);
            Account toAccount = bank.retrieveAccount(commandArgs[2]);
            return (fromAccount.canTransfer() && toAccount.canTransfer());
        }
        return false;
    }

    private boolean idIsValid(String fromId, String toId) {
        try {
            Integer.parseInt(fromId);
            Integer.parseInt(toId);
            return true;
        } catch (NumberFormatException error) {
            return false;
        }
    }

    private boolean validAmountToTransfer(String amount) {
        try {
            double checkAmount = Double.parseDouble(amount);
            return (checkAmount >= 0);
        } catch (NumberFormatException error) {
            return false;
        }
    }
}
