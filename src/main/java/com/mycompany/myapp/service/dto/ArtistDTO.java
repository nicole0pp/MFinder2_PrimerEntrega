package com.mycompany.myapp.service.dto;
import java.time.Duration;
import javax.validation.constraints.*;

import com.mycompany.myapp.domain.User;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Song} entity.
 */
public class ArtistDTO implements Serializable {

    private Long id;

    @NotNull
    private String artistic_name;

    private User user;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistic_name() {
        return artistic_name;
    }

    public void setArtistic_name(String artistic_name) {
        this.artistic_name = artistic_name;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
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
            ", name='" + getArtistic_name() + "'" +
            ", user_id='" + getUser() + 
            "}";
    }
}

