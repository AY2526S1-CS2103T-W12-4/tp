package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.CommandHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * An Immutable CommandHistory that is serializable to JSON format.
 */
@JsonRootName(value = "commandhistory")
class JsonSerializableCommandHistory {

    private static final Logger LOGGER = LogsCenter.getLogger(JsonCommandHistoryStorage.class);

    private final List<String> commands = new ArrayList<>();


    /**
     * Constructs a {@code JsonSerializableCommandHistory} with the given commands.
     */
    @JsonCreator
    public JsonSerializableCommandHistory(@JsonProperty("commands") List<String> commands) {
        this.commands.addAll(commands);
    }

    /**
     * Converts a given {@code CommandHistory} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableCommandHistory}.
     */
    public JsonSerializableCommandHistory(CommandHistory source) {
        commands.addAll(source.getCommandList().stream().toList());
    }

}
