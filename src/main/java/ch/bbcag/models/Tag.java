package ch.bbcag.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "name is required")
  @NotBlank(message = "item name can't be empty")
  private String name;

  @ManyToMany(mappedBy = "linkedTags")
  @JsonBackReference
  private Set<Item> linkedItems = new HashSet<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Item> getLinkedItems() {
    return linkedItems;
  }

  public void setLinkedItems(Set<Item> linkedItems) {
    this.linkedItems = linkedItems;
  }
}
