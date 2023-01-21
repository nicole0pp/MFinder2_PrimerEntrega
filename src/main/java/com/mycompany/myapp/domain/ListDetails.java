package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ListDetails.
 */
@Entity
@Table(name = "list_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ListDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private ReproductionLists list;

    @OneToMany(mappedBy = "listDetails")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Songs> songs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReproductionLists getList() {
        return list;
    }

    public ListDetails list(ReproductionLists reproductionLists) {
        this.list = reproductionLists;
        return this;
    }

    public void setList(ReproductionLists reproductionLists) {
        this.list = reproductionLists;
    }

    public Set<Songs> getSongs() {
        return songs;
    }

    public ListDetails songs(Set<Songs> songs) {
        this.songs = songs;
        return this;
    }

    public ListDetails addSong(Songs songs) {
        this.songs.add(songs);
        songs.setListDetails(this);
        return this;
    }

    public ListDetails removeSong(Songs songs) {
        this.songs.remove(songs);
        songs.setListDetails(null);
        return this;
    }

    public void setSongs(Set<Songs> songs) {
        this.songs = songs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListDetails)) {
            return false;
        }
        return id != null && id.equals(((ListDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ListDetails{" +
            "id=" + getId() +
            "}";
    }
}
