package ch.bbcag.repositories;

import ch.bbcag.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface TagRepository extends CrudRepository<Tag, Integer> {

  @Query("SELECT i FROM Tag i WHERE i.name LIKE CONCAT('%', :name, '%')")
  Iterable<Tag> findByName(@Param("name") String name);
}
