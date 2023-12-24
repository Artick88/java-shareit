package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, List<Item>> itemUserIdMap = new HashMap<>();
    private final Map<Long, Item> itemIdMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public List<Item> getAll(Long userId) {
        return new ArrayList<>(itemUserIdMap.get(userId));
    }

    @Override
    public Item get(Long itemId) {
        return itemIdMap.get(itemId);
    }

    @Override
    public Item create(Item item) {
        item.setId(++id);
        List<Item> items = itemUserIdMap.getOrDefault(item.getOwner().getId(), new ArrayList<>());
        items.add(item);

        itemUserIdMap.put(item.getOwner().getId(), items);
        itemIdMap.put(item.getId(), item);

        return item;
    }

    @Override
    public Item update(Item item) {

        Item itemFromMap = itemIdMap.get(item.getId());

        Item itemSaved = Item.builder()
                .id(item.getId())
                .name(item.getName() == null ? itemFromMap.getName() : item.getName())
                .description(item.getDescription() == null ? itemFromMap.getDescription() : item.getDescription())
                .available(item.getAvailable() == null ? itemFromMap.getAvailable() : item.getAvailable())
                .owner(item.getOwner() == null ? itemFromMap.getOwner() : item.getOwner())
                .build();

        List<Item> items = itemUserIdMap.getOrDefault(itemSaved.getOwner().getId(), new ArrayList<>());
        items.removeIf(it -> it.getId().equals(itemSaved.getId()));
        items.add(itemSaved);


        itemIdMap.put(itemSaved.getId(), itemSaved);
        itemUserIdMap.put(itemSaved.getOwner().getId(), items);

        return itemIdMap.get(item.getId());
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(itemIdMap.values());
    }
}
