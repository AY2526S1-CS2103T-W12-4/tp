package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Integration tests for {@link EditPropertyCommand} exercising interactions with the real {@link ModelManager}.
 */
public class EditPropertyCommandIntegrationTest {

    /**
     * Ensures constructing the command with a null index throws {@link NullPointerException}.
     */
    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("N"));
        assertThrows(NullPointerException.class, () -> new EditPropertyCommand(null, desc));
    }

    /**
     * Ensures constructing the command with a null descriptor throws {@link NullPointerException}.
     */
    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        Index idx = Index.fromOneBased(1);
        assertThrows(NullPointerException.class, () -> new EditPropertyCommand(idx, null));
    }

    /**
     * Verifies that an empty descriptor is reported as not editing any field.
     */
    @Test
    public void edit_noFieldsProvided_descriptorIsAnyFieldEditedFalse() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        assertEquals(false, desc.isAnyFieldEdited());
    }

    /**
     * Verifies that a descriptor with at least one field set is reported as editing fields.
     */
    @Test
    public void edit_someFieldsProvided_descriptorIsAnyFieldEditedTrue() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("New"));
        assertEquals(true, desc.isAnyFieldEdited());
    }

    /**
     * Edits the name of an existing property successfully and validates output message and updated fields.
     */
    @Test
    public void execute_validIndex_success() throws Exception {
        AddressBook ab = new AddressBook();
        Property original = new Property(new Address("A"), new Price(1000), new PropertyName("P"));
        ab.addProperty(original);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("Q"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandResult result = cmd.execute(model);

        Property edited = model.getFilteredPropertyList().get(0);
        String expectedMessage = String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS,
                Messages.formatProperty(edited));
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals("Q", edited.getPropertyName().toString());
        assertEquals(1000, edited.getPrice().getIntegerPrice());
        assertEquals("A", edited.getAddress().toString());
    }

    /**
     * Editing with an invalid index throws {@link CommandException}.
     */
    @Test
    public void execute_invalidIndex_throwsCommandException() {
        AddressBook ab = new AddressBook();
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("Q"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    /**
     * Editing to collide exactly with another property's identity triggers duplicate error.
     */
    @Test
    public void execute_duplicateProperty_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Property propA = new Property(new Address("A1"), new Price(100), new PropertyName("NameA"));
        Property propB = new Property(new Address("A2"), new Price(200), new PropertyName("NameB"));
        ab.addProperty(propA);
        ab.addProperty(propB);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("NameB"));
        desc.setAddress(new Address("A2"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, ex.getMessage());
    }

    /**
     * Editing to the same name as the current property's name, ignoring case, triggers duplicate error.
     */
    @Test
    public void execute_sameNameAsSelfCaseInsensitive_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Property original = new Property(new Address("A"), new Price(1_000_000), new PropertyName("Sunny Villa"));
        ab.addProperty(original);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("sunny villa"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, ex.getMessage());
    }

    /**
     * Editing to a name that equals another property's name ignoring case triggers duplicate error.
     */
    @Test
    public void execute_caseClashWithOther_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Property target = new Property(new Address("A1"), new Price(10), new PropertyName("Alpha"));
        Property other = new Property(new Address("A2"), new Price(20), new PropertyName("Sunny Villa"));
        ab.addProperty(target);
        ab.addProperty(other);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("SUNNY VILLA"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, ex.getMessage());
    }

    /**
     * Editing to a name that differs from another property only by spacing succeeds with a warning in the feedback.
     */
    @Test
    public void execute_spaceOnlyClashWithOther_succeedsWithWarning() throws Exception {
        AddressBook ab = new AddressBook();
        Property target = new Property(new Address("A1"), new Price(10), new PropertyName("Alpha"));
        Property other = new Property(new Address("A2"), new Price(20), new PropertyName("Sunny Villa"));
        ab.addProperty(target);
        ab.addProperty(other);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("sunny      villa"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandResult result = cmd.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Warning: A similar property name already exists"));
        Property edited = model.getFilteredPropertyList().get(0);
        assertEquals("sunny      villa", edited.getPropertyName().toString());
    }
}
