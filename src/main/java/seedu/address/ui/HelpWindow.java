package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String BUTTON_MESSAGE = "Copy URL";
    public static final String COPY_SUCCESSFUL_MESSAGE = "URL Copied!";
    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w12-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "EstateSearch Help\n\n"
            + "========================\n"
            + "  COMMAND SUMMARY\n"
            + "========================\n\n"

            // Managing Contacts
            + "--- Managing Contacts ---\n"
            + "add: Adds a new client to EstateSearch.\n"
            + "  Format: add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...\n"
            + "  Example: add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2 t/buyer\n\n"

            + "edit: Edits an existing client by their index number.\n"
            + "  Format: edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...\n"
            + "  Example: edit 1 p/91234567 e/new.email@example.com\n\n"

            + "delete: Deletes a client by their index number.\n"
            + "  Format: delete PERSON INDEX\n"
            + "  Example: delete 2\n\n"

            + "clear: Clears all clients from the address book.\n"
            + "  Format: clear\n"
            + "  >> WARNING: This command is irreversible! <<\n\n"

            //Managing Properties
            + "--- Managing Properties ---\n"
            + "addp: Adds a new property to EstateSearch.\n"
            + "  Format: addp a/ADDRESS pr/PRICE n/PROPERTY NAME\n"
            + "  Example: addp a/123, Example St, #01-01 pr/500000 n/Hillside Villa\n\n"

            + "deletep: Deletes a property by their index number.\n"
            + "  Format: deletep PROPERTY INDEX\n"
            + "  Example: deletep 2\n\n"

            + "editp: Edits an existing property by their index number.\n"
            + "  Format: editp INDEX [n/PROPERTY NAME] [a/ADDRESS] [pr/PRICE]\n"
            + "  Example: editp 1 pr/550000 a/456, New St, #02-02\n\n"

            // Managing client-property relations
            + "--- Managing Client-Property Relations ---\n"
            + "setip: Sets an interested property for a client.\n"
            + "  Format: setip i/PERSON INDEX n/PROPERTY NAME \n"
            + "  Example: setip i/1 n/Hillside Villa\n\n"

            + "setop: Sets an owned property for a client.\n"
            + "  Format: setop o/PERSON INDEX n/PROPERTY NAME \n"
            + "  Example: setop o/1 n/Hillside Villa\n\n"

            + "deleteip: Deletes an interested property from a client.\n"
            + "  Format: deleteip i/PERSON INDEX n/PROPERTY NAME \n"
            + "  Example: deleteip i/1 n/Hillside Villa\n\n"

            + "deleteop: Deletes an owned property from a client.\n"
            + "  Format: deleteop i/PERSON INDEX n/PROPERTY NAME \n"
            + "  Example: deleteop i/1 n/Hillside Villa\n\n"

            // Viewing Data
            + "--- Viewing Data ---\n"
            + "list: Shows a list of all clients.\n"
            + "  Format: list\n\n"

            + "listp: Shows a list of all properties.\n"
            + "  Format: listp\n\n"

            + "find: Finds clients who match all given criteria (case-insensitive).\n"
            + "  Format: find [n/KEYWORD]... [t/KEYWORD]...\n"
            + "  Example: find n/Alex t/buyer\n\n"

            + "findp: Finds properties that match all given criteria (case-insensitive).\n"
            + "  Format: findp n/PROPERTY NAME\n"
            + "  Example: findp n/Chapel Hill House\n\n"

            // Application Commands
            + "--- Application ---\n"
            + "help: Shows this help menu.\n"
            + "  Format: help\n\n"

            + "exit: Exits the application.\n"
            + "  Format: exit\n\n";

    public static final String URL_MESSAGE = "For more information refer to the user guide:";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Label urlMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        urlMessage.setText(URL_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        copyButton.setText(BUTTON_MESSAGE);
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
        copyButton.setText(COPY_SUCCESSFUL_MESSAGE);
    }
}
