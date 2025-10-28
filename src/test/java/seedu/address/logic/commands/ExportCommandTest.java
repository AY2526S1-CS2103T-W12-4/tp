package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.*;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ExportCommandTest {

    @Test
    public void execute_exportWithPeople_createsCsvFile() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new CommandHistory());
        model.addPerson(ALICE);

        String filename = "test_export";
        ExportCommand command = new ExportCommand(filename);

        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, filename + ".csv"),
                result.getFeedbackToUser());

        File file = new File(System.getProperty("user.dir") + "/data/" + filename + ".csv");
        assertEquals(true, file.exists());

        file.delete();
    }

    @Test
    public void execute_exportWithNoPeople_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new CommandHistory());
        ExportCommand command = new ExportCommand("empty_export");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_multipleTags_exportsCorrectly() throws Exception {
        Person multiTagPerson = new PersonBuilder(ALICE)
                .withTags("HighPriority", "CashBuyer", "VIP")
                .build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new CommandHistory());
        model.addPerson(multiTagPerson);

        String filename = "multitag_export";
        ExportCommand command = new ExportCommand(filename);

        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, filename + ".csv"),
                result.getFeedbackToUser());

        File csvFile = new File(System.getProperty("user.dir") + "/data/" + filename + ".csv");
        assertEquals(true, csvFile.exists());

        csvFile.delete();
    }

    @Test
    public void execute_fileAlreadyExists_overwritesSuccessfully() throws Exception {
        String filename = "overwrite_export";
        File file = new File(System.getProperty("user.dir") + "/data/" + filename + ".csv");

        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("dummy content");
        }

        Person person = new PersonBuilder(ALICE).build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs(), new CommandHistory());
        model.addPerson(person);

        ExportCommand command = new ExportCommand(filename);
        CommandResult result = command.execute(model);

        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, 1, filename + ".csv"),
                result.getFeedbackToUser());

        assertEquals(true, file.exists());

        file.delete();
    }

}
