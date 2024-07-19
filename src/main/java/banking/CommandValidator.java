package banking;

public class CommandValidator {

    public final Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        String[] commandArgs =  command.split(" ");
        try {
            if (commandArgs[0].equalsIgnoreCase("create")) {
                CreateCommandValidator createValidator = new CreateCommandValidator(bank);
                return createValidator.validateCreate(commandArgs);
            } else if (commandArgs[0].equalsIgnoreCase("deposit")) {
                DepositCommandValidator depositValidator = new DepositCommandValidator(bank);
                return depositValidator.validateDeposit(commandArgs);
            } else if (commandArgs[0].equalsIgnoreCase("withdraw")) {
                WithdrawCommandValidator withdrawValidator = new WithdrawCommandValidator(bank);
                return withdrawValidator.validateWithdraw(commandArgs);
            } else if (commandArgs[0].equalsIgnoreCase("transfer")) {
                TransferCommandValidator transferValidator = new TransferCommandValidator(bank);
                return transferValidator.validateTransfer(commandArgs);
            } else if (commandArgs[0].equalsIgnoreCase("pass")) {
                PassCommandValidator passValidator = new PassCommandValidator(bank);
                return passValidator.validatePass(commandArgs);
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException error) {
            return false;
        }
    }
}
