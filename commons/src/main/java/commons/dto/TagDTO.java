package commons.dto;

import java.util.Objects;

public class TagDTO {

    private int eventId;

    private String name;

    private String colour;

    public TagDTO(int eventId, String name, String colour) {
        this.eventId = eventId;
        this.name = name;
        this.colour = colour;
    }
    public TagDTO() {

    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return eventId == tagDTO.eventId && Objects.equals(name, tagDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name);
    }
}
