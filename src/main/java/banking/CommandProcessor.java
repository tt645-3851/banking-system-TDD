package banking;

public class CommandProcessor {

    public Bank bank;
    private String[] commandArgs;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String command) {
        commandArgs = command.split(" ");
        if (commandArgs[0].equalsIgnoreCase("create")){
            CreateCommandProcessor createProcessor = new CreateCommandProcessor(bank);
            createProcessor.createAccount(commandArgs);
        } else if (commandArgs[0].equalsIgnoreCase("deposit")){
            DepositCommandProcessor depositProcessor = new DepositCommandProcessor(bank);
            depositProcessor.depositAccount(commandArgs);
        } else if (commandArgs[0].equalsIgnoreCase("withdraw")) {
            WithdrawCommandProcessor withdrawProcessor = new WithdrawCommandProcessor(bank);
            withdrawProcessor.withdrawAccount(commandArgs);
        } else if (commandArgs[0].equalsIgnoreCase("transfer")) {
            TransferCommandProcessor transferProcessor = new TransferCommandProcessor(bank);
            transferProcessor.executeTransfer(commandArgs);
        } else if (commandArgs[0].equalsIgnoreCase("pass")) {
            PassCommandProcessor passProcessor = new PassCommandProcessor(bank);
            passProcessor.executePass(commandArgs);
        }
    }
}
