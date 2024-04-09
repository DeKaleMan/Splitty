package server.database;

import commons.Participant;
import commons.ParticipantId;
import commons.Tag;
import commons.TagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByTagIdIs(TagId id);
}
