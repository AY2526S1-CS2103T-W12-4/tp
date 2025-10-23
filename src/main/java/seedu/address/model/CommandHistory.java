package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Class to store all the Command History of the user.
 */
public class CommandHistory {
    private final ObservableList<String> commands;

    /**
     * Constructs an empty {@code CommandHistory}.
     */
    public CommandHistory() {
        commands = FXCollections.observableArrayList();
    }

    /**
     * Constructs a {@code CommandHistory} with given commands.
     */
    @JsonCreator
    public CommandHistory(@JsonProperty("commandList") List<String> commands) {
        this.commands = FXCollections.observableArrayList(commands);
    }

    /**
     * Adds a command to the list.
     */
    public void add(String newCommand) {
        requireNonNull(newCommand);
        commands.add(newCommand);
    }

    /**
     * Gets a List of last {@param n} commands
     * @param n Number of commands to get
     * @return ObservableList of last {@param n} commands.
     */
    public ObservableList<String> getCommandList(int n) throws IllegalValueException {
        int size = commands.size();
        if (n < 0) {
            throw new IllegalValueException("Number of Commands cannot be less than 0!");
        }
        return FXCollections.observableArrayList(
                commands.subList(Math.max(size - n, 0), size)
        );
    }

    /**
     * Gets a List of commands
     * @return ObservableList of all command.
     */

    public ObservableList<String> getCommands() {
        return FXCollections.observableArrayList(commands);
    }
}
