package banking;

public class CreateCommandValidator extends CommandValidator {

    public CreateCommandValidator(Bank bank) {
        super(bank);
    }

    public boolean validateCreate(String[] commandArgs) {
        if (commandArgs.length == 4 && accountCheckingOrSavings(commandArgs[1])) {
            return accountIdIsCorrect(commandArgs[2]) && checkCorrectAPR(commandArgs[3]);
        } else {
            return checkCorrectAPR(commandArgs[3]) && checkCDBalance(commandArgs[4]) && accountIdIsCorrect(commandArgs[2]);
        }
    }

    public boolean accountIdIsCorrect(String accountId) {
        try {
            Integer.parseInt(accountId);
            return (bank.accountExistsById(accountId) && accountId.length() == 8);
        } catch (NumberFormatException error) {
            return false;
        }
    }

    public boolean checkCorrectAPR(String aprString) {
        double apr;
        try {
            apr = Double.parseDouble(aprString);
            return (apr >= 0 && apr <= 10);
        } catch (NumberFormatException error) {
            return false;
        }
    }

    public boolean checkCDBalance(String cdBalance) {
        double balance;
        try {
            balance = Double.parseDouble(cdBalance);
            return (balance >= 1000 && balance <= 10000);
        } catch (NumberFormatException error) {
            return false;
        }
    }


    public boolean accountCheckingOrSavings(String accountType) {
        return (accountType.equalsIgnoreCase("checking") || accountType.equalsIgnoreCase("savings"));
    }

}
