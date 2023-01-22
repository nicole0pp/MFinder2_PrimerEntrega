package com.mycompany.myapp.service.dto;
import java.time.Duration;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Song} entity.
 */
public class SongDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] picture;

    private String pictureContentType;
    private Duration duration;

    @Lob
    private byte[] audio;

    private String audioContentType;
    private String artists;


    private Long musicGenreId;

    private Long albumId;

    private Long listDetailsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public String getAudioContentType() {
        return audioContentType;
    }

    public void setAudioContentType(String audioContentType) {
        this.audioContentType = audioContentType;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public Long getMusicGenreId() {
        return musicGenreId;
    }

    public void setMusicGenreId(Long MusicGenreId) {
        this.musicGenreId = MusicGenreId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getListDetailsId() {
        return listDetailsId;
    }

    public void setListDetailsId(Long listDetailsId) {
        this.listDetailsId = listDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SongDTO SongDTO = (SongDTO) o;
        if (SongDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), SongDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SongDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picture='" + getPicture() + "'" +
            ", duration='" + getDuration() + "'" +
            ", audio='" + getAudio() + "'" +
            ", artists='" + getArtists() + "'" +
            ", musicGenre=" + getMusicGenreId() +
            ", Album=" + getAlbumId() +
            ", listDetails=" + getListDetailsId() +
            "}";
    }
}
