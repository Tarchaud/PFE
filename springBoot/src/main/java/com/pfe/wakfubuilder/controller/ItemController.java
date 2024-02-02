package com.pfe.wakfubuilder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.Item;
import com.pfe.wakfubuilder.service.ItemService;


@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/items")
    public List<Item> getItems() {
        return itemService.getItems();
    }

    @RequestMapping("/item/{id}")
    public Item getItem(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value ="item/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "items")
    public void addItem(@RequestBody Item item) {
        itemService.addItem(item);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "item/{id}")
    public void updateItem(@PathVariable long id, @RequestBody Item item) {
        itemService.updateItem(id, item);
    }
}
