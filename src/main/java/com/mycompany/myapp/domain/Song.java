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
 * A Song.
 */
@Entity
@Table(name = "Song")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Song implements Serializable {

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
    private MusicGenre musicGenre;

    @ManyToOne
    @JsonIgnoreProperties("Song")
    private Album Album;

    @ManyToOne
    @JsonIgnoreProperties("Song")
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

    public Song name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Song picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public Song pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Duration getDuration() {
        return duration;
    }

    public Song duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public byte[] getAudio() {
        return audio;
    }

    public Song audio(byte[] audio) {
        this.audio = audio;
        return this;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public String getAudioContentType() {
        return audioContentType;
    }

    public Song audioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
        return this;
    }

    public void setAudioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
    }

    public String getArtists() {
        return artists;
    }

    public Song artists(String artists) {
        this.artists = artists;
        return this;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public MusicGenre getMusicGenre() {
        return musicGenre;
    }

    public Song musicGenre(MusicGenre MusicGenre) {
        this.musicGenre = MusicGenre;
        return this;
    }

    public void setMusicGenre(MusicGenre MusicGenre) {
        this.musicGenre = MusicGenre;
    }

    public Album getAlbum() {
        return Album;
    }

    public Song Album(Album Album) {
        this.Album = Album;
        return this;
    }

    public void setAlbum(Album Album) {
        this.Album = Album;
    }

    public ListDetails getListDetails() {
        return listDetails;
    }

    public Song listDetails(ListDetails listDetails) {
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
        if (!(o instanceof Song)) {
            return false;
        }
        return id != null && id.equals(((Song) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Song{" +
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
