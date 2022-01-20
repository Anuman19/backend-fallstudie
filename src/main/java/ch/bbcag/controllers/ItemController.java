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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

  @Autowired ItemRepository itemRepository;

  @DeleteMapping("{id}")
  public void delete(@PathVariable Integer id) {
    itemRepository.deleteById(id);
  }

  @GetMapping("{id}")
  public Item findById(@PathVariable Integer id) {
    return itemRepository.findById(id).orElseThrow();
  }

  @PostMapping(consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public void insert(@RequestBody Item item) {
    itemRepository.save(item);
  }

  @PutMapping(consumes = "application/json")
  public void update(@RequestBody Item item) {
    itemRepository.save(item);
  }
}
