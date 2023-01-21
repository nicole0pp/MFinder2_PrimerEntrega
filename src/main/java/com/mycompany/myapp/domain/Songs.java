package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

/**
 * A Songs.
 */
@Entity
@Table(name = "songs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Songs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "duration")
    private Duration duration;

    @Lob
    @Column(name = "audio")
    private byte[] audio;

    @Column(name = "audio_content_type")
    private String audioContentType;

    @Column(name = "artists")
    private String artists;

    @OneToOne
    @JoinColumn(unique = true)
    private MusicGenres musicGenre;

    @ManyToOne
    @JsonIgnoreProperties("songs")
    private Albums album;

    @ManyToOne
    @JsonIgnoreProperties("songs")
    private ListDetails listDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Songs name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Songs picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public Songs pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Duration getDuration() {
        return duration;
    }

    public Songs duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public byte[] getAudio() {
        return audio;
    }

    public Songs audio(byte[] audio) {
        this.audio = audio;
        return this;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public String getAudioContentType() {
        return audioContentType;
    }

    public Songs audioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
        return this;
    }

    public void setAudioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
    }

    public String getArtists() {
        return artists;
    }

    public Songs artists(String artists) {
        this.artists = artists;
        return this;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public MusicGenres getMusicGenre() {
        return musicGenre;
    }

    public Songs musicGenre(MusicGenres musicGenres) {
        this.musicGenre = musicGenres;
        return this;
    }

    public void setMusicGenre(MusicGenres musicGenres) {
        this.musicGenre = musicGenres;
    }

    public Albums getAlbum() {
        return album;
    }

    public Songs album(Albums albums) {
        this.album = albums;
        return this;
    }

    public void setAlbum(Albums albums) {
        this.album = albums;
    }

    public ListDetails getListDetails() {
        return listDetails;
    }

    public Songs listDetails(ListDetails listDetails) {
        this.listDetails = listDetails;
        return this;
    }

    public void setListDetails(ListDetails listDetails) {
        this.listDetails = listDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Songs)) {
            return false;
        }
        return id != null && id.equals(((Songs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Songs{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", duration='" + getDuration() + "'" +
            ", audio='" + getAudio() + "'" +
            ", audioContentType='" + getAudioContentType() + "'" +
            ", artists='" + getArtists() + "'" +
            "}";
    }
}
