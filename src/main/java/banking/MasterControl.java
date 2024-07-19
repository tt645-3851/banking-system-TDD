package banking;

import java.util.ArrayList;
import java.util.List;

public class MasterControl {
    private final CommandValidator commandValidator;
    private final CommandProcessor commandProcessor;
    private final CommandStorage commandStorage;

    public MasterControl(CommandValidator commandValidator,
                         CommandProcessor commandProcessor, CommandStorage commandStorage) {
        this.commandValidator = commandValidator;
        this.commandProcessor = commandProcessor;
        this.commandStorage = commandStorage;
    }

    public List<String> start(ArrayList<String> input) {
        for (String command : input) {
            if (commandValidator.validate(command)) {
                commandProcessor.process(command);
                commandStorage.addToValidCommandsList(command);
            } else {
                commandStorage.addToInvalidCommandsList((command));
            }
        }
        return commandStorage.getOutput();
    }

}
