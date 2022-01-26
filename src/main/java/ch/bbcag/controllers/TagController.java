package ch.bbcag.controllers;

import ch.bbcag.models.Tag;
import ch.bbcag.repositories.TagRepository;
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
public class TagController {

  @Autowired private TagRepository tagRepository;

  @GetMapping
  public Iterable<Tag> findByName(@RequestParam(required = false) String name) {

    if (name == null) {
      return tagRepository.findAll();
    } else {
      return tagRepository.findByName(name);
    }
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Integer id) {
    try {
      tagRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @GetMapping(path = "{id}")
  public Tag findById(@PathVariable Integer id) {
    try {
      return tagRepository.findById(id).orElseThrow();
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
    }
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = "application/json")
  public void insert(@Valid @RequestBody Tag newTag) {

    try {
      tagRepository.save(newTag);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }

  @PutMapping(consumes = "application/json")
  public void update(@Valid @RequestBody Tag tag) {
    try {
      tagRepository.save(tag);
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }
}
