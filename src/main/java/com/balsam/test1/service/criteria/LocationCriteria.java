package com.balsam.test1.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.balsam.test1.domain.Location} entity. This class is used
 * in {@link com.balsam.test1.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddressLine1;

    private StringFilter streetAddressLine2;

    private StringFilter latitude;

    private StringFilter longitude;

    public LocationCriteria() {}

    public LocationCriteria(LocationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.streetAddressLine1 = other.streetAddressLine1 == null ? null : other.streetAddressLine1.copy();
        this.streetAddressLine2 = other.streetAddressLine2 == null ? null : other.streetAddressLine2.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStreetAddressLine1() {
        return streetAddressLine1;
    }

    public StringFilter streetAddressLine1() {
        if (streetAddressLine1 == null) {
            streetAddressLine1 = new StringFilter();
        }
        return streetAddressLine1;
    }

    public void setStreetAddressLine1(StringFilter streetAddressLine1) {
        this.streetAddressLine1 = streetAddressLine1;
    }

    public StringFilter getStreetAddressLine2() {
        return streetAddressLine2;
    }

    public StringFilter streetAddressLine2() {
        if (streetAddressLine2 == null) {
            streetAddressLine2 = new StringFilter();
        }
        return streetAddressLine2;
    }

    public void setStreetAddressLine2(StringFilter streetAddressLine2) {
        this.streetAddressLine2 = streetAddressLine2;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public StringFilter latitude() {
        if (latitude == null) {
            latitude = new StringFilter();
        }
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public StringFilter longitude() {
        if (longitude == null) {
            longitude = new StringFilter();
        }
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddressLine1, that.streetAddressLine1) &&
            Objects.equals(streetAddressLine2, that.streetAddressLine2) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddressLine1, streetAddressLine2, latitude, longitude);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (streetAddressLine1 != null ? "streetAddressLine1=" + streetAddressLine1 + ", " : "") +
            (streetAddressLine2 != null ? "streetAddressLine2=" + streetAddressLine2 + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            "}";
    }
}
