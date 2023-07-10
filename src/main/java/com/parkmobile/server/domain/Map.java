package com.parkmobile.server.domain;

import com.parkmobile.server.domain.enumeration.ZoneMap;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Map.
 */
@Document(collection = "map")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("location")
    private String location;

    @Field("zone_map")
    private ZoneMap zoneMap;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Map id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public Map location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZoneMap getZoneMap() {
        return this.zoneMap;
    }

    public Map zoneMap(ZoneMap zoneMap) {
        this.setZoneMap(zoneMap);
        return this;
    }

    public void setZoneMap(ZoneMap zoneMap) {
        this.zoneMap = zoneMap;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        return id != null && id.equals(((Map) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Map{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", zoneMap='" + getZoneMap() + "'" +
            "}";
    }
}
