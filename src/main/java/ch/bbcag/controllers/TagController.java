package ch.bbcag.controllers;

import ch.bbcag.models.Tag;
import ch.bbcag.repositories.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.NoSuchElementException;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tags")
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "403",
          description = "You do not have permission to do this. Please use /login first.",
          content = {@Content(mediaType = "application/json")})
    })
public class TagController {

  @Autowired private TagRepository tagRepository;

  @GetMapping
  @Operation(summary = "Find tags with a given name. If no name given, all tags are returned.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag(s) found",
            content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))
            })
      })
  public Iterable<Tag> findByName(
      @Parameter(description = "Tag name to search") @RequestParam(required = false) String name) {

    if (name == null) {
      return tagRepository.findAll();
    } else {
      return tagRepository.findByName(name);
    }
  }

  @DeleteMapping("{id}")
  @Operation(summary = "Delete Tag by ID.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag(s) found",
            content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found.",
            content = {
              @Content(mediaType = "application/json")
            })
      })
  public void delete(@Parameter(description = "Tag ID to delete.") @PathVariable Integer id) {
    try {
      tagRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @GetMapping(path = "{id}")
  @Operation(summary = "Find tags with a given id.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag(s) found",
            content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found.",
            content = {
              @Content(mediaType = "application/json")
            })
      })
  public Tag findById(@Parameter(description = "Tag ID to search") @PathVariable Integer id) {
    try {
      return tagRepository.findById(id).orElseThrow();
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = "application/json")
  @Operation(summary = "Insert a Tag.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Tag(s) created.",
            content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {
              @Content(mediaType = "application/json")
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = {
              @Content(mediaType = "application/json")
            })
      })
  public void insert(@Parameter(description = "New Tag.") @Valid @RequestBody Tag newTag) {

    try {
      tagRepository.save(newTag);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PutMapping(consumes = "application/json")
  @Operation(summary = "Update given Tag")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag(s) found",
            content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found.",
            content = {
              @Content(mediaType = "application/json")
            })
      })
  public void update(@Parameter(description = "Tag to be updated.") @Valid @RequestBody Tag tag) {
    try {
      tagRepository.save(tag);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }
}
