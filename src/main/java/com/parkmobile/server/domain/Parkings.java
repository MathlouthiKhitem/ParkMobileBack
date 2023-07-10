package com.parkmobile.server.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Parkings.
 */
@Document(collection = "parkings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parkings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("zone_title")
    private String zoneTitle;

    @Field("numero_parking")
    private String numeroParking;

    @Field("duree")
    private String duree;

    @Field("price")
    private String price;

    @Field("start_date")
    private String startDate;

    @Field("end_date")
    private String endDate;

    @DBRef
    @Field("users")
    private Users users;

    @DBRef
    @Field("map")
    private Map map;
    @Field("matricule")
    private String matricule;

    // Getter and Setter for matricule
    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Parkings id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneTitle() {
        return this.zoneTitle;
    }

    public Parkings zoneTitle(String zoneTitle) {
        this.setZoneTitle(zoneTitle);
        return this;
    }

    public void setZoneTitle(String zoneTitle) {
        this.zoneTitle = zoneTitle;
    }

    public String getNumeroParking() {
        return this.numeroParking;
    }

    public Parkings numeroParking(String numeroParking) {
        this.setNumeroParking(numeroParking);
        return this;
    }

    public void setNumeroParking(String numeroParking) {
        this.numeroParking = numeroParking;
    }

    public String getDuree() {
        return this.duree;
    }

    public Parkings duree(String duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getPrice() {
        return this.price;
    }

    public Parkings price(String price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public Parkings startDate(String startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public Parkings endDate(String endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Parkings users(Users users) {
        this.setUsers(users);
        return this;
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Parkings map(Map map) {
        this.setMap(map);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parkings)) {
            return false;
        }
        return id != null && id.equals(((Parkings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parkings{" +
            "id=" + getId() +
            ", zoneTitle='" + getZoneTitle() + "'" +
            ", numeroParking='" + getNumeroParking() + "'" +
            ", duree='" + getDuree() + "'" +
            ", price='" + getPrice() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
