package ch.bbcag.repositories;

import ch.bbcag.models.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Integer> {

    ApplicationUser findByUsername(String username);

}
