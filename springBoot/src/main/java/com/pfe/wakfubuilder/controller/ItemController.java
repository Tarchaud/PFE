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
}
