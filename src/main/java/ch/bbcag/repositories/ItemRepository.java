package ch.bbcag.repositories;

import ch.bbcag.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends CrudRepository<Item, Integer> {

  @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :name, '%')")
  Iterable<Item> findByName(@Param("name") String name);

  @Query("SELECT i from Item i JOIN i.linkedTags t WHERE t.name LIKE CONCAT('%', :tagName, '%')")
  Iterable<Item> findByTagName(@Param("tagName") String tagName);

  @Query(
      "SELECT i from Item i JOIN i.linkedTags t WHERE t.name LIKE CONCAT('%', :tagName, '%') AND  i.name LIKE CONCAT('%', :name, '%')")
  Iterable<Item> findByNameAndTagName(@Param("name") String name, @Param("tagName") String tagName);
}
