package ch.bbcag.controllers;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.services.ApplicationUserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

import java.util.NoSuchElementException;

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
        try {
            return applicationUserService.findById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found");
        }
    }

    @PostMapping(value = "/sign-up", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody ApplicationUser newUser) {
        try {
            applicationUserService.signUp(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }


    @PutMapping(consumes = "application/json")
    public void update(@RequestBody ApplicationUser user) {
        try {
            applicationUserService.update(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        try {
            applicationUserService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be deleted");
        }
    }

}
