package com.mycompany.myapp.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Artist.
 */
@Entity
@Table(name = "artist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "artistic_name", nullable = false, unique = true)
    private String artistic_name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    @OneToOne
    @JoinColumn(unique=true)
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistic_name() {
        return artistic_name;
    }

    public Artist artistic_name(String artistic_name) {
        this.artistic_name = artistic_name;
        return this;
    }

    public void setArtistic_name(String artistic_name) {
        this.artistic_name = artistic_name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artist)) {
            return false;
        }
        return id != null && id.equals(((Artist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + getId() +
            ", artistic_name='" + getArtistic_name() + "'" +
            "}";
    }
}
