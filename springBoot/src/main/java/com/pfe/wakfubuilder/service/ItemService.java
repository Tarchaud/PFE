package com.pfe.wakfubuilder.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Item;

@Service
public class ItemService {

    private List<Item> items;

    public ItemService() {
        this.items = new ArrayList<>();
        loadItemsFromJson();
    }

    private void loadItemsFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("db/items.json");
            Item[] itemsArray = objectMapper.readValue(resource.getInputStream(), Item[].class);
            items.addAll(Arrays.asList(itemsArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(long id) {
        return items.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public void deleteItem(long id) {
        items.removeIf(item -> item.getId() == id);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void updateItem(long id, Item item) {
        items.forEach(itemlambda -> {
            if (itemlambda.getId() == id) {
                items.set(items.indexOf(itemlambda), item);
            }
        });
    }
}
