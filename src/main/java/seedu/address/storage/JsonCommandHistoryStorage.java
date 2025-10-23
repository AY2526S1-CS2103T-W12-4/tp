package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.CommandHistory;

/**
 * A class to access CommandHistory data stored as a json file on the hard disk.
 */
public class JsonCommandHistoryStorage implements CommandHistoryStorage {

    private final Path filePath;

    public JsonCommandHistoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getCommandHistoryFilePath() {
        return filePath;
    }

    @Override
    public Optional<CommandHistory> readCommandHistory() throws DataLoadingException {
        return readCommandHistory(filePath);
    }

    /**
     * Similar to {@link #readCommandHistory()}
     * @param cmdHistoryFilePath location of the data. Cannot be null.
     * @throws DataLoadingException if the file format is not as expected.
     */
    public Optional<CommandHistory> readCommandHistory(Path cmdHistoryFilePath) throws DataLoadingException {
        return JsonUtil.readJsonFile(cmdHistoryFilePath, CommandHistory.class);
    }

    @Override
    public void saveCommandHistory(CommandHistory cmdHistory) throws IOException {
        JsonUtil.saveJsonFile(cmdHistory, filePath);
    }

}
