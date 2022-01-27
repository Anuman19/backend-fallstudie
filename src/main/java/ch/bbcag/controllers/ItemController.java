package ch.bbcag.controllers;

import ch.bbcag.models.Item;
import ch.bbcag.repositories.ItemRepository;
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
@RequestMapping("/items")
public class ItemController {

  @Autowired private ItemRepository itemRepository;

  @GetMapping(path = "{id}")
  @Operation(summary = "Find items with a given id.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item(s) found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Item not found.",
            content = {@Content(mediaType = "application/json")})
      })
  public Item findById(@Parameter(description = "Item ID to search") @PathVariable Integer id) {
    try {
      return itemRepository.findById(id).orElseThrow();
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = "application/json")
  @Operation(summary = "Insert a Item.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Item(s) created.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            })
      })
  public void insert(@Parameter(description = "Item to insert.") @Valid @RequestBody Item newItem) {

    try {
      itemRepository.save(newItem);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PutMapping(consumes = "application/json")
  @Operation(summary = "Update given Item")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item(s) found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Item not found.",
            content = {@Content(mediaType = "application/json")})
      })
  public void update(@Parameter(description = "Item to update.") @Valid @RequestBody Item item) {
    try {
      itemRepository.save(item);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @DeleteMapping("{id}")
  @Operation(summary = "Delete Item by ID.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item(s) found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Item not found.",
            content = {@Content(mediaType = "application/json")})
      })
  public void delete(@Parameter(description = "Delete an Item by ID.") @PathVariable Integer id) {
    try {
      itemRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @GetMapping
  @Operation(summary = "Find Item with a given name. If no name given, all Items are returned.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item(s) found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Item.class))
            })
      })
  public Item findByNameAndTagName(
      @RequestParam(required = false) String name, @RequestParam(required = false) String tagName) {
    if (name.isBlank() && tagName.isBlank()) {
      return (Item) itemRepository.findAll();
    } else if (!name.isBlank() && !tagName.isBlank()) {
      return (Item) itemRepository.findByNameAndTagName(name, tagName);
    } else if (!name.isBlank()) {
      return (Item) itemRepository.findByName(name);
    } else {
      return (Item) itemRepository.findByTagName(tagName);
    }
  }
}
