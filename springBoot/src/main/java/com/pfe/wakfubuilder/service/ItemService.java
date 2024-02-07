package com.pfe.wakfubuilder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pfe.wakfubuilder.model.Item;
import com.pfe.wakfubuilder.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public Item getItem(String id) {
        if (id == null) {
            return null;
        }
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem.orElse(null);
    }

    public List<Item> getItemsByEquipmentItemTypeIds(List<Integer> ids) {
        return itemRepository.findByEquipmentItemTypeIds(ids);
    }
}
