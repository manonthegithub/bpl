package ru.bookpleasure.db.entities;

import javax.persistence.Embeddable;

/**
 * Created by Kirill on 09/07/16.
 */
@Embeddable
public class Address {

    private String city;
    private String street;
    private String building;
    private String suite;
    private String flat;
    private String zip;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("city='").append(city).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", building='").append(building).append('\'');
        sb.append(", suite='").append(suite).append('\'');
        sb.append(", flat='").append(flat).append('\'');
        sb.append(", zip='").append(zip).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!city.equals(address.city)) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (building != null ? !building.equals(address.building) : address.building != null) return false;
        if (suite != null ? !suite.equals(address.suite) : address.suite != null) return false;
        if (flat != null ? !flat.equals(address.flat) : address.flat != null) return false;
        return zip != null ? zip.equals(address.zip) : address.zip == null;

    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (building != null ? building.hashCode() : 0);
        result = 31 * result + (suite != null ? suite.hashCode() : 0);
        result = 31 * result + (flat != null ? flat.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }
}
