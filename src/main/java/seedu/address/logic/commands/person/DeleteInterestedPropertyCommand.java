package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_PROPERTY_NOT_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Deletes a property from the specified person's list of interested properties.
 */
public class DeleteInterestedPropertyCommand extends Command {

    public static final String COMMAND_WORD = "deleteip";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a specified property from the selected person's "
            + "list of interested properties.\n"
            + "Parameters: "
            + PREFIX_INDEX + "INDEX "
            + PREFIX_NAME + "PROPERTY NAME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INDEX + "1 "
            + PREFIX_NAME + "Sunshine Villa";

    private static final String MESSAGE_SUCCESS = "Deleted property: %1$s from %2$s's list of interested properties.";

    private final PropertyName targetPropertyName;
    private final Index targetPersonIndex;

    /**
     * Creates a DeleteInterestedPropertyCommand to delete {@code Property} from the specified {@code Person}
     */
    public DeleteInterestedPropertyCommand(PropertyName propertyName, Index personIndex) {
        requireNonNull(propertyName);
        requireNonNull(personIndex);
        this.targetPropertyName = propertyName;
        this.targetPersonIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = model.getFilteredPersonList().get(targetPersonIndex.getZeroBased());

        Property toDelete = model.getAddressBook().getPropertyList().stream()
                .filter(property -> property.getPropertyName()
                        .fullName.equals(targetPropertyName.getFullName()))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PROPERTY_NOT_FOUND,
                        targetPropertyName.getFullName())));

        if (!targetPerson.getInterestedProperties().contains(toDelete)) {
            throw new CommandException(String.format("The property %1$s is not in %2$s's list of interested "
                    + "properties.", toDelete.getPropertyName().toString(), targetPerson.getName().toString()));
        }

        Person updatedPerson = targetPerson.removeInterestedProperty(toDelete);
        model.setPerson(targetPerson, updatedPerson);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS, toDelete.getPropertyName().toString(), targetPerson.getName()));
    }

    /**
     * Returns the success message of this command.
     */
    public static String getSuccessMessage() {
        return MESSAGE_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteInterestedPropertyCommand)) {
            return false;
        }

        DeleteInterestedPropertyCommand otherDeleteCommand = (DeleteInterestedPropertyCommand) other;
        return targetPropertyName.equals(otherDeleteCommand.targetPropertyName)
                && targetPersonIndex.equals(otherDeleteCommand.targetPersonIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPropertyName", targetPropertyName)
                .add("targetPersonIndex", targetPersonIndex)
                .toString();
    }
}
