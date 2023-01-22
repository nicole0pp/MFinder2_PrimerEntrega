package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @ManyToOne
    @JsonIgnoreProperties("ListDetails")
    private FavoriteList List;
    // @JoinColumn(unique = true)
    // private FavoriteList list;

    @OneToMany(mappedBy = "listDetails")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Song> Song = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FavoriteList getList() {
        return List;
    }

    public ListDetails list(FavoriteList FavoriteList) {
        this.List = FavoriteList;
        return this;
    }

    public void setList(FavoriteList FavoriteList) {
        this.List = FavoriteList;
    }

    public Set<Song> getSong() {
        return Song;
    }

    public ListDetails Song(Set<Song> Song) {
        this.Song = Song;
        return this;
    }

    public ListDetails addSong(Song Song) {
        this.Song.add(Song);
        Song.setListDetails(this);
        return this;
    }

    public ListDetails removeSong(Song Song) {
        this.Song.remove(Song);
        Song.setListDetails(null);
        return this;
    }

    public void setSong(Set<Song> Song) {
        this.Song = Song;
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
