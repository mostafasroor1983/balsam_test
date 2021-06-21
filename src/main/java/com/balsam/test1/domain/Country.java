package com.balsam.test1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private String name;

    @Size(max = 3)
    @Column(name = "code", length = 3, unique = true)
    private String code;

    @Size(max = 6)
    @Column(name = "phone_code", length = 6, unique = true)
    private String phoneCode;

    @Size(max = 6)
    @Column(name = "membership_code", length = 6, unique = true)
    private String membershipCode;

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Set<City> cities = new HashSet<>();

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Set<Corporate> corporates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Country code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneCode() {
        return this.phoneCode;
    }

    public Country phoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
        return this;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getMembershipCode() {
        return this.membershipCode;
    }

    public Country membershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
        return this;
    }

    public void setMembershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
    }

    public Set<City> getCities() {
        return this.cities;
    }

    public Country cities(Set<City> cities) {
        this.setCities(cities);
        return this;
    }

    public Country addCities(City city) {
        this.cities.add(city);
        city.setCountry(this);
        return this;
    }

    public Country removeCities(City city) {
        this.cities.remove(city);
        city.setCountry(null);
        return this;
    }

    public void setCities(Set<City> cities) {
        if (this.cities != null) {
            this.cities.forEach(i -> i.setCountry(null));
        }
        if (cities != null) {
            cities.forEach(i -> i.setCountry(this));
        }
        this.cities = cities;
    }

    public Set<Corporate> getCorporates() {
        return this.corporates;
    }

    public Country corporates(Set<Corporate> corporates) {
        this.setCorporates(corporates);
        return this;
    }

    public Country addCorporates(Corporate corporate) {
        this.corporates.add(corporate);
        corporate.setCountry(this);
        return this;
    }

    public Country removeCorporates(Corporate corporate) {
        this.corporates.remove(corporate);
        corporate.setCountry(null);
        return this;
    }

    public void setCorporates(Set<Corporate> corporates) {
        if (this.corporates != null) {
            this.corporates.forEach(i -> i.setCountry(null));
        }
        if (corporates != null) {
            corporates.forEach(i -> i.setCountry(this));
        }
        this.corporates = corporates;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            ", membershipCode='" + getMembershipCode() + "'" +
            "}";
    }
}
