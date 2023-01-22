package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ListDetails} entity.
 */
public class ListDetailsDTO implements Serializable {

    private Long id;


    private Long listId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long FavoriteListId) {
        this.listId = FavoriteListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListDetailsDTO listDetailsDTO = (ListDetailsDTO) o;
        if (listDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), listDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ListDetailsDTO{" +
            "id=" + getId() +
            ", list=" + getListId() +
            "}";
    }
}
