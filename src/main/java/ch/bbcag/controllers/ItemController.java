package ch.bbcag.controllers;

import ch.bbcag.models.Item;
import ch.bbcag.repositories.ItemRepository;
import java.util.NoSuchElementException;
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

import javax.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/items")
public class ItemController {

  @Autowired private ItemRepository itemRepository;

  @GetMapping(path = "{id}")
  public Item findById(@PathVariable Integer id) {
    try {
      itemRepository.findById(id);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return itemRepository.findById(id).orElseThrow();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = "application/json")
  public void insert(@Valid @RequestBody Item newItem) {

    try {
      itemRepository.save(newItem);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PutMapping(consumes = "application/json")
  public void update(@Valid @RequestBody Item item) {
    try {
      itemRepository.save(item);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Integer id) {
    try {
      itemRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

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
