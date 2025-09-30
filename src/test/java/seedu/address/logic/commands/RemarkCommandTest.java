package seedu.address.logic.commands;

import seedu.address.logic.commands.RemarkCommand;

public class RemarkCommandTest {
    @Test
    public void execute() {
        assertCommandFailure(new RemarkCommand(), model, MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
