package org.example.crudapp.controllers;

import org.example.crudapp.models.Item;
import org.example.crudapp.repositories.ItemRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Item> getAll() {
        return repository.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Item create(@RequestBody Item item) {
        return repository.save(item);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) {
        item.setId(id);
        return repository.save(item);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}