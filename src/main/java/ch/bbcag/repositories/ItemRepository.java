package ch.bbcag.repositories;

import ch.bbcag.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends CrudRepository<Item, Integer> {

    @Query("SELECT i FROM Item i WHERE i.name LIKE CONCAT('%', :name, '%')")
    Iterable<Item> findByName(@Param("name") String name);

}
