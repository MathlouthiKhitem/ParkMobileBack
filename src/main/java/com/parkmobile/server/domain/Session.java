package com.parkmobile.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Session.
 */
@Document(collection = "session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @Field("location")
    private String location;

    public boolean getStatus() {
        return status;
    }
    public boolean setStatus(boolean status) {
        this.status = status;
        return status;
    }
    @Field("status")
    private boolean status;
    @DBRef
    @Field("clients")
    @JsonIgnoreProperties(value = { "sessions" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Session id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Session startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }



    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Session endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return this.location;
    }

    public Session location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeSession(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addSession(this));
        }
        this.clients = clients;
    }

    public Session clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Session addClient(Client client) {
        this.clients.add(client);
        client.getSessions().add(this);
        return this;
    }

    public Session removeClient(Client client) {
        this.clients.remove(client);
        client.getSessions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return id != null && id.equals(((Session) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Session{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
