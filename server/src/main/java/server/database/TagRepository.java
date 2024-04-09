package server.database;

import commons.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByTagId(TagId id);
    @Query("SELECT t FROM Event e JOIN Tag t on e.id = t.tagId.event.id WHERE e.id = ?1")
    List<Tag> findTagsByEventId(int eventId);

}
