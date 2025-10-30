package seedu.address.storage;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Jackson-friendly version of {@link Property}.
 */
class JsonAdaptedProperty {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Property's %s field is missing!";

    private final String address;
    private final Integer price;
    private final String propertyName;

    /**
     * Constructs a {@code JsonAdaptedProperty} with the given property details.
     */
    @JsonCreator
    public JsonAdaptedProperty(@JsonProperty("address") String address, @JsonProperty("price") Integer price,
            @JsonProperty("propertyName") String propertyName) {
        this.address = address;
        this.price = price;
        this.propertyName = propertyName;

    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedProperty(Property source) {
        this.address = source.getAddress().toString();
        this.price = source.getPrice().getIntegerPrice();
        this.propertyName = source.getPropertyName().toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Property} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted property.
     */
    public Property toModelType() throws IllegalValueException {
        if (propertyName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PropertyName.class.getSimpleName()));
        }
        if (!PropertyName.isValidName(propertyName)) {
            throw new IllegalValueException(PropertyName.MESSAGE_CONSTRAINTS);
        }
        final PropertyName modelName = new PropertyName(propertyName);

        if (price == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Price.class.getSimpleName()));
        }
        if (!Price.isValidPrice(price)) {
            throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
        }
        final Price modelPrice = new Price(price);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        return new Property(modelAddress, modelPrice, modelName);
    }

    /**
     * Returns a compact JSON string representing this adapted Property, or "{}" if
     * serialization fails. Intended for diagnostics in load reports.
     */
    public String toRawJsonString() {
        try {
            return seedu.address.commons.util.JsonUtil.toJsonString(this);
        } catch (Exception e) {
            return "{}";
        }
    }

    public java.util.Set<String> invalidFieldKeys() {
        java.util.Set<String> keys = new java.util.HashSet<>();
        if (propertyName == null || propertyName.trim().isEmpty() || !PropertyName.isValidName(propertyName)) {
            keys.add("propertyName");
        }
        if (address == null || address.trim().isEmpty() || !Address.isValidAddress(address)) {
            keys.add("address");
        }
        if (price == null || !Price.isValidPrice(price)) {
            keys.add("price");
        }
        return keys;
    }

    // --- Add these getters so the loader can prefill the dialog with originals ---
    public String getName() {
        return propertyName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonAdaptedProperty)) {
            return false;
        }
        JsonAdaptedProperty that = (JsonAdaptedProperty) o;
        return Objects.equals(propertyName, that.propertyName)
                && Objects.equals(address, that.address)
                && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                propertyName, address, price
        );
    }
}
