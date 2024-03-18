package commons;
import java.util.Objects;
public class EventDTO {

    public int id;
    private String name;
    private String date;
    private String owner;
    private String description;

    public EventDTO(String name, String date,
                 String owner,
                 String description) {
        this.name = name; // name of the event
        this.date = date; // date of when the event occurred/was created
        this.owner = owner; // the person that created the event
        this.description = description; //description of the event
    }

    public EventDTO() {

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO eventDTO = (EventDTO) o;
        return id == eventDTO.id && Objects.equals(name, eventDTO.name)
                && Objects.equals(date, eventDTO.date)
                && Objects.equals(owner, eventDTO.owner)
                && Objects.equals(description, eventDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, owner, description);
    }

    @Override
    public String toString() {
        String res = "This is event with name: "
                + name
                + " that is created on "
                + date
                + " the person that created is: "
                + owner
                + " the description is: "
                + description;
        return res;
    }
}
