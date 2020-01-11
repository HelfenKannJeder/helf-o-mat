package de.helfenkannjeder.helfomat.core.organisation;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Valentin Zickner
 */
public class OrganisationId implements Serializable {

    private static final Pattern VALID_UUID = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    @SuppressWarnings("CanBeFinal")
    private String value;

    public OrganisationId() {
        this(UUID.randomUUID().toString());
    }

    public OrganisationId(String value) {
        Preconditions.checkArgument(VALID_UUID.matcher(value).matches(), "Invalid OrganisationId provided");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationId that = (OrganisationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "OrganisationId{" +
            "value='" + value + '\'' +
            '}';
    }

}
