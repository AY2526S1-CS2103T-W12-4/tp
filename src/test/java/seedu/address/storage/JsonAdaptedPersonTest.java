package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Property;
import seedu.address.model.util.SampleDataUtil;

/**
 * Tests for JsonAdaptedPerson covering validation, field mapping, and diagnostics.
 */
public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_LISTING = " ";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_LISTING = BENSON.getListing().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<Property> VALID_INTERESTED_PROPERTIES =
            new ArrayList<>(SampleDataUtil.getSampleProperties());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_LISTING, VALID_TAGS,
                        VALID_INTERESTED_PROPERTIES);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_LISTING, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_LISTING,
                        VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_LISTING, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_LISTING,
                        VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_LISTING, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_LISTING,
                        VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_LISTING, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_LISTING,
                        invalidTags, VALID_INTERESTED_PROPERTIES);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullListing_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Listing.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidListing_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                INVALID_LISTING, VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String expectedMessage = Listing.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void ctor_fromPerson_gettersMatchSource() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON.getName().fullName, adapted.getName());
        assertEquals(BENSON.getPhone().value, adapted.getPhone());
        assertEquals(BENSON.getEmail().value, adapted.getEmail());
        assertEquals(BENSON.getAddress().value, adapted.getAddress());
        assertEquals(BENSON.getListing().value, adapted.getListing());
    }

    @Test
    public void invalidFieldKeys_allInvalidFieldsReported() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                INVALID_NAME, INVALID_PHONE, INVALID_EMAIL, INVALID_ADDRESS, INVALID_LISTING, null,
                VALID_INTERESTED_PROPERTIES);
        Set<String> keys = adapted.invalidFieldKeys();
        org.junit.jupiter.api.Assertions.assertTrue(keys.contains("name"));
        org.junit.jupiter.api.Assertions.assertTrue(keys.contains("phone"));
        org.junit.jupiter.api.Assertions.assertTrue(keys.contains("email"));
        org.junit.jupiter.api.Assertions.assertTrue(keys.contains("address"));
        org.junit.jupiter.api.Assertions.assertTrue(keys.contains("listing"));
        assertEquals(5, keys.size());
    }

    @Test
    public void invalidFieldKeys_allValid_returnsEmptySet() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_LISTING,
                VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        Set<String> keys = adapted.invalidFieldKeys();
        org.junit.jupiter.api.Assertions.assertTrue(keys.isEmpty());
    }

    @Test
    public void toRawJsonString_returnsParsableCompactJson() throws Exception {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_LISTING,
                VALID_TAGS, VALID_INTERESTED_PROPERTIES);
        String json = adapted.toRawJsonString();
        boolean hasBraces = json.trim().startsWith("{") && json.trim().endsWith("}");
        boolean hasNameKey = json.contains("\"name\"");
        boolean hasPhoneKey = json.contains("\"phone\"");
        boolean hasEmailKey = json.contains("\"email\"");
        boolean hasAddressKey = json.contains("\"address\"");
        boolean hasListingKey = json.contains("\"listing\"");
        org.junit.jupiter.api.Assertions.assertTrue(hasBraces
                && hasNameKey && hasPhoneKey && hasEmailKey && hasAddressKey && hasListingKey);
    }

    @Test
    public void isFullyValid_validPerson_returnsTrue() {
        Person p = BENSON;
        org.junit.jupiter.api.Assertions.assertTrue(p.isFullyValid());
    }

    @Test
    public void toModelType_invalidComposite_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_NAME, INVALID_PHONE, INVALID_EMAIL, INVALID_ADDRESS,
                INVALID_LISTING, null, VALID_INTERESTED_PROPERTIES);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
