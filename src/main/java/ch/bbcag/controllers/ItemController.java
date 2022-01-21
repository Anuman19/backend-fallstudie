package ch.bbcag.controllers;

import ch.bbcag.models.Item;
import ch.bbcag.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/items")
public class ItemController {

  @Autowired private ItemRepository itemRepository;

  @GetMapping(path = "{id}")
  public Item findById(@PathVariable Integer id) {
    return itemRepository.findById(id).orElseThrow();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = "application/json")
  public void insert(@Valid @RequestBody Item newItem) {
    itemRepository.save(newItem);
  }

  @PutMapping(consumes = "application/json")
  public void update(@Valid @RequestBody Item item) {
    itemRepository.save(item);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Integer id) {
    itemRepository.deleteById(id);
  }

  public Item findByNameAndTagName(@RequestParam(required = false) String name, String tagName) {
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
