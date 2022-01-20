package ch.bbcag.controllers;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.services.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ApplicationUserService applicationUserService;

    @GetMapping
    public Iterable<ApplicationUser> findAll() {
        return applicationUserService.findAll();
    }

    @GetMapping("{id}")
    public ApplicationUser findById(@PathVariable Integer id) {
        return applicationUserService.findById(id);
    }

    @PostMapping(value = "/sign-up", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody ApplicationUser newUser) {
        applicationUserService.signUp(newUser);
    }


    @PutMapping(consumes = "application/json")
    public void update(@RequestBody ApplicationUser user) {
        applicationUserService.update(user);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        applicationUserService.deleteById(id);
    }

}
