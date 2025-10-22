package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;


/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Listing("Condo"),
                getTagSet("friends"), new HashSet<>()),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Listing("HDB"),
                getTagSet("colleagues", "friends"), getSampleProperties()),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Listing("Landed Property"),
                getTagSet("neighbours"), getSampleProperties(1)),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Listing("Condo"),
                getTagSet("family"), getSampleProperties(2)),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Listing("HDB"),
                getTagSet("classmates"), getSampleProperties(2)),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Listing("Landed Property"),
                getTagSet("colleagues"), new HashSet<>())
        };
    }

    /**
     * Returns a set of sample properties
     */
    public static Set<Property> getSampleProperties() {
        return getSampleProperties(3);
    }

    /**
     * Returns a set of sample properties
     * @param n number of sample properties to return
     */
    public static Set<Property> getSampleProperties(int n) {
        if (n <= 0) {
            return new HashSet<>();
        }
        if (n >= 3) {
            n = 3;
        }
        return Arrays.stream(new Property[] {
            new Property(
                    new seedu.address.model.property.Address("123 Example St"),
                    new Price(500000),
                    new seedu.address.model.property.Name("Sunny Villa")),
            new Property(
                    new seedu.address.model.property.Address("45 Orchard Rd"),
                    new Price(1200000),
                    new seedu.address.model.property.Name("City Loft")),
            new Property(
                    new seedu.address.model.property.Address("7 Bukit Timah"),
                    new Price(2000000),
                    new seedu.address.model.property.Name("Hilltop Mansion"))
        }).limit(n).collect(Collectors.toSet());
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Property sampleProp : getSampleProperties()) {
            sampleAb.addProperty(sampleProp);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
