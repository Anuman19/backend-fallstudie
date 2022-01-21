package ch.bbcag.utils;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.models.Item;
import ch.bbcag.models.Tag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDataUtil {

    public static ApplicationUser getTestUser() {
        return getTestUsers().get(0);
    }
    public static ApplicationUser getInvalidTestUser() {
        ApplicationUser user = new ApplicationUser();
        user.setId(0);
        return user;
    }

    public static List<ApplicationUser> getTestUsers() {
        List<ApplicationUser> users = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            ApplicationUser user = new ApplicationUser();
            user.setId(i);
            user.setUsername("User" + i);
            user.setPassword("Password" + i);
            users.add(user);
        }

        return users;
    }

    public static Item getTestItem() {
        return getTestItems().get(0);
    }

    public static List<Item> getTestItems() {
        List<Item> items = new ArrayList<>();
        HashSet<Tag> tags = new HashSet<>();
        List<ApplicationUser> users = getTestUsers();


        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Tag");
        tags.add(tag);

        for (int i = 1; i <= 4; i++) {
            Item item = new Item();
            item.setId(i);
            item.setName("Item" + i);
            item.setDescription("Description" + i);
            item.setCreatedAt(Timestamp.valueOf("2020-01-01 00:00:00"));
            item.setDoneAt(Timestamp.valueOf("2020-01-01 05:00:00"));
            item.setDeletedAt(Timestamp.valueOf("2020-01-01 10:00:00"));
            item.setApplicationUser(users.get(0));
            if (i > 3) {
                item.setApplicationUser(users.get(1));
            }
            tag.getLinkedItems().add(item);
            item.setLinkedTags(tags);
            items.add(item);
        }


        return items;
    }

    public static Tag getTestTag() {
        return getTestTags().get(0);
    }

    public static List<Tag> getTestTags() {
        List<Tag> tags = new ArrayList<>();
        Set<Item> items = new HashSet<>();

        Item item = new Item();
        item.setId(1);
        item.setName("Item");
        item.setApplicationUser(getTestUser());
        items.add(item);

        for (int i = 1; i <= 4; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setName("Tag" + i);
            tag.setLinkedItems(items);
            item.getLinkedTags().add(tag);
            tags.add(tag);
        }

        return tags;
    }


}
