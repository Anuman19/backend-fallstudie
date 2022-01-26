package ch.bbcag.controllers;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.services.ApplicationUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import javax.validation.Valid;
import java.util.NoSuchElementException;

@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "You do not have permission to do this. Please use /login first.",
                content = {@Content(mediaType = "application/json")})})
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ApplicationUserService applicationUserService;


    @Operation(summary = "Find all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))})})
    @GetMapping
    public Iterable<ApplicationUser> findAll() {
        return applicationUserService.findAll();
    }


    @Operation(summary = "Find a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("{id}")
    public ApplicationUser findById(@Parameter(description = "Id of user to get") @PathVariable Integer id) {
        try {
            return applicationUserService.findById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found");
        }
    }


    @Operation(summary = "Create a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))}),
    })
    @PostMapping(value = "/sign-up", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Parameter(description = "The new user to create") @Valid @RequestBody ApplicationUser newUser) {
        try {
            applicationUserService.signUp(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }


    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))}),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))})})
    @PutMapping(consumes = "application/json")
    public void update(@Parameter(description = "The user to update") @Valid @RequestBody ApplicationUser user) {
        try {
            applicationUserService.update(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }


    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUser.class))}),
            @ApiResponse(responseCode = "404", description = "User could not be deleted",
                    content = @Content)})
    @DeleteMapping("{id}")
    public void delete(@Parameter(description = "Id of user to delete") @PathVariable Integer id) {
        try {
            applicationUserService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be deleted");
        }
    }

}
