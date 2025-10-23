package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;

public class CommandHistory
{
    private final ObservableList<String> commandList;

    /**
     * Constructs an empty {@code CommandHistory}.
     */
    public CommandHistory() {
        commandList = FXCollections.observableArrayList();
    }

    /**
     * Constructs a {@code CommandHistory} with given commands.
     */
    @JsonCreator
    public CommandHistory(@JsonProperty("commandList") List<String> commands) {
        commandList = FXCollections.observableArrayList(commands);
    }

    /**
     * Gets the list of commands for JSON serialization.
     */
    @JsonProperty("commandList")
    public List<String> getCommands() {
        return new ArrayList<>(commandList);
    }

    /**
     * Adds a command to the list.
     */
    public void add(String newCommand) {
        requireNonNull(newCommand);
        commandList.add(newCommand);
    }

    /**
     * Gets a List of last {@param n} commands
     * @param n Number of commands to get
     * @return ObservableList of last {@param n} commands.
     */
    public ObservableList<String> getCommandList(int n) throws IllegalValueException {
        int size = commandList.size();
        if (n < 0) {
            throw new IllegalValueException("Number of Commands cannot be less than 0!");
        }
        return FXCollections.observableArrayList(
                commandList.subList(Math.max(size - n, 0), size)
        );
    }

    /**
     * Gets a List of commands
     * @return ObservableList of all command.
     */

    public ObservableList<String> getCommandList() {
        return FXCollections.observableArrayList(commandList);
    }
}
