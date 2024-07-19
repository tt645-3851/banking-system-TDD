package banking;

public class PassCommandValidator extends CommandValidator{

    public PassCommandValidator(Bank bank) {
        super(bank);
    }


    public boolean validatePass(String[] commandArgs) {
        if (commandArgs.length == 2) {
            return (validMonthsPassed(commandArgs[1]));
        }
        return false;
    }

    private boolean validMonthsPassed(String commandArg) {
        try {
            int monthsPassed = Integer.parseInt(commandArg);
            return (monthsPassed >= 1 && monthsPassed <= 60);
        } catch (NumberFormatException error) {
            return false;
        }
    }
}
