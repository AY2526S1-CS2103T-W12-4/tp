package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

import java.util.logging.Logger;

/**
 * Controller for a command history page
 */
public class CommandHistoryWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(CommandHistoryWindow.class);
    private static final String FXML = "CommandHistoryWindow.fxml";

    @FXML
    private Label commandMessage;

    private static String commandList;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public CommandHistoryWindow(Stage root) {
        super(FXML, root);
        commandMessage.setText(commandList);
    }

    /**
     * Creates a new HelpWindow.
     */
    public CommandHistoryWindow(CommandHistory historyLst) {
        this(new Stage());
        commandList = "Here is a list of commands\n" + String.join("\n", historyLst.getCommandList(5));
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
        logger.fine("Showing last five commands");
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
}
