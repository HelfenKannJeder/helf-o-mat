package de.helfenkannjeder.helfomat.core.organization;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Valentin Zickner
 */
@Embeddable
public class OrganizationId implements Serializable {

    private static final Pattern VALID_UUID = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    @SuppressWarnings("CanBeFinal")
    @Column(name = "organizationId")
    private String value;

    public OrganizationId() {
        this(UUID.randomUUID().toString());
    }

    public OrganizationId(String value) {
        Preconditions.checkArgument(VALID_UUID.matcher(value).matches(), "Invalid OrganizationId provided");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationId that = (OrganizationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "OrganizationId{" +
            "value='" + value + '\'' +
            '}';
    }

}
