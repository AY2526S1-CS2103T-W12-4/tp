package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.person.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        List<String> keywords = Arrays.asList("Alice", "Bob");
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(keywords), "name", "Alice Bob");
        assertParseSuccess(parser, "n/Alice Bob", expectedFindCommand);
        assertParseSuccess(parser, "n/Alice  Bob", expectedFindCommand);
    }

    @Test
    public void parse_emptyTagValue_throwsParseException() {
        assertParseFailure(parser, "t/", FindCommandParser.MESSAGE_MISSING_TAG);
        assertParseFailure(parser, "t/   ", FindCommandParser.MESSAGE_MISSING_TAG);
    }

    @Test
    public void parse_nameAndTagCombined_throwsParseException() {
        assertParseFailure(parser, "n/ t/", FindCommandParser.MESSAGE_TOO_MANY_PARAMETERS);

        assertParseFailure(parser, "n/Alice t/friends",
                FindCommandParser.MESSAGE_TOO_MANY_PARAMETERS);

        assertParseFailure(parser, "t/friends n/Alice",
                FindCommandParser.MESSAGE_TOO_MANY_PARAMETERS);
    }

}
