package seedu.address.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonCommandHistoryStorage;

/**
 * Integration tests for {@link StorageManager}.
 */
public class StorageManagerTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonCommandHistoryStorage cmdHistoryStorage = new JsonCommandHistoryStorage(getTempFilePath("cmd"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, cmdHistoryStorage);
    }

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);

        UserPrefs retrieved = storageManager.readUserPrefs().get();
        Assertions.assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        AddressBook original = seedu.address.testutil.TypicalPersons.getTypicalAddressBook();
        storageManager.saveAddressBook(original);

        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        Assertions.assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        Assertions.assertNotNull(storageManager.getAddressBookFilePath());
    }

    /**
     * File with only invalid entries should produce an empty model and some invalids
     * (unless the implementation repairs them all, in which case the accounting check still passes).
     */
    @Test
    public void readAddressBookWithReport_invalidOnly_allAccounted() throws Exception {
        Path invalidOnly = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");

        StorageManager mgr = new StorageManager(
                new JsonAddressBookStorage(invalidOnly),
                new JsonUserPrefsStorage(getTempFilePath("prefs2")),
                new JsonCommandHistoryStorage(getTempFilePath("cmd2"))
        );

        LoadReport report = mgr.readAddressBookWithReport(invalidOnly);

        AddressBook loaded = report.getModelData().getAddressBook();
        Assertions.assertNotNull(loaded, "Loaded AddressBook should not be null.");

        int validCount = loaded.getPersonList().size();
        int invalidCount = report.getInvalids().size();
        int totalInJson = countPersonsArray(invalidOnly);

        Assertions.assertEquals(totalInJson, validCount + invalidCount,
                "Valid + invalid should equal number of raw JSON entries.");
    }

    /**
     * Mixed file: ensure the model has some valid entries and that
     * valid + invalid equals the raw JSON entry count, regardless of
     * whether the implementation quarantines or repairs.
     */
    @Test
    public void readAddressBookWithReport_mixed_allAccounted() throws Exception {
        Path mixed = TEST_DATA_FOLDER.resolve("invalidAndValidPersonAddressBook.json");

        StorageManager mgr = new StorageManager(
                new JsonAddressBookStorage(mixed),
                new JsonUserPrefsStorage(getTempFilePath("prefs3")),
                new JsonCommandHistoryStorage(getTempFilePath("cmd3"))
        );

        LoadReport report = mgr.readAddressBookWithReport(mixed);
        AddressBook loaded = report.getModelData().getAddressBook();

        Assertions.assertNotNull(loaded, "Loaded AddressBook should not be null.");
        Assertions.assertFalse(loaded.getPersonList().isEmpty(),
                "Loaded AddressBook should contain at least one valid person.");

        int validCount = loaded.getPersonList().size();
        int invalidCount = report.getInvalids().size();
        int totalInJson = countPersonsArray(mixed);

        Assertions.assertEquals(totalInJson, validCount + invalidCount,
                "Valid + invalid should equal number of raw JSON entries.");
    }

    @Test
    public void readAddressBookWithReport_noArg_usesConfiguredPath() throws Exception {
        class RecordingAddressBookStorage implements AddressBookStorage {
            final Path configuredPath;
            private Path lastPath;

            RecordingAddressBookStorage(Path configuredPath) {
                this.configuredPath = configuredPath;
            }

            Path getLastPath() {
                return lastPath;
            }

            @Override
            public Path getAddressBookFilePath() {
                return configuredPath;
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook() {
                return Optional.empty();
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook(Path file) {
                return Optional.empty();
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path file) {
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
            }

            @Override
            public LoadReport readAddressBookWithReport(Path file) {
                this.lastPath = file;
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList()
                );
            }

            @Override
            public LoadReport overwriteRawEntryAtIndex(
                    int index, seedu.address.model.person.Person person) {
                throw new UnsupportedOperationException("not used");
            }
        }

        Path configured = getTempFilePath("delegation-ab.json");
        RecordingAddressBookStorage recordingAb = new RecordingAddressBookStorage(configured);

        JsonUserPrefsStorage prefs = new JsonUserPrefsStorage(getTempFilePath("delegation-prefs.json"));

        StorageManager mgr = new StorageManager(
                recordingAb,
                prefs,
                new JsonCommandHistoryStorage(getTempFilePath("delegation-cmd.json"))
        );

        LoadReport report = mgr.readAddressBookWithReport();

        Assertions.assertNotNull(report.getModelData().getAddressBook(), "Report should contain model data.");
        Assertions.assertEquals(configured, recordingAb.getLastPath(),
                "No-arg readAddressBookWithReport() must delegate using getAddressBookFilePath().");
    }

    /** Counts how many person objects exist in the raw JSON "persons" array. */
    private static int countPersonsArray(Path jsonPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        JsonNode root = mapper.readTree(jsonPath.toFile());
        JsonNode persons = (root == null) ? null : root.get("persons");
        return (persons != null && persons.isArray()) ? persons.size() : 0;
    }
}
