package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.Ingredient;
import sk.ukf.pizzeria.entity.Pizza;
import sk.ukf.pizzeria.entity.PizzaSize;
import sk.ukf.pizzeria.entity.Tag;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.IngredientRepository;
import sk.ukf.pizzeria.repository.PizzaRepository;
import sk.ukf.pizzeria.repository.PizzaSizeRepository;
import sk.ukf.pizzeria.repository.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaSizeRepository pizzaSizeRepository;
    private final IngredientRepository ingredientRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, 
                       PizzaSizeRepository pizzaSizeRepository,
                       IngredientRepository ingredientRepository,
                       TagRepository tagRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaSizeRepository = pizzaSizeRepository;
        this.ingredientRepository = ingredientRepository;
        this.tagRepository = tagRepository;
    }

    public List<Pizza> findAll() {
        return pizzaRepository.findAll();
    }

    public List<Pizza> findAllActive() {
        return pizzaRepository.findByActiveTrue();
    }

    public Page<Pizza> findAllActivePaginated(Pageable pageable) {
        return pizzaRepository.findByActiveTrue(pageable);
    }

    public Pizza findById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pizza s ID " + id + " neexistuje"));
    }

    public Pizza findBySlug(String slug) {
        return pizzaRepository.findBySlug(slug)
                .orElseThrow(() -> new ObjectNotFoundException("Pizza s URL " + slug + " neexistuje"));
    }

    public List<Pizza> search(String keyword) {
        return pizzaRepository.searchByKeyword(keyword);
    }

    public Page<Pizza> searchPaginated(String keyword, Pageable pageable) {
        return pizzaRepository.searchByKeywordPaginated(keyword, pageable);
    }

    public List<Pizza> findByTag(Long tagId) {
        return pizzaRepository.findByTagId(tagId);
    }

    public Pizza save(Pizza pizza) {
        ensureUniqueSlug(pizza, null);
        return pizzaRepository.save(pizza);
    }

    public Pizza saveWithRelations(Pizza pizza, List<Long> ingredientIds, List<Long> tagIds) {
        // Vygenerovanie unikátneho slugu
        ensureUniqueSlug(pizza, null);
        
        // Nastavenie ingrediencií
        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            Set<Ingredient> ingredients = new HashSet<>(ingredientRepository.findAllById(ingredientIds));
            pizza.setIngredients(ingredients);
        } else {
            pizza.setIngredients(new HashSet<>());
        }

        // Nastavenie tagov
        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            pizza.setTags(tags);
        } else {
            pizza.setTags(new HashSet<>());
        }

        return pizzaRepository.save(pizza);
    }

    public Pizza update(Long id, Pizza updatedPizza) {
        Pizza pizza = findById(id);
        pizza.setName(updatedPizza.getName());
        pizza.setDescription(updatedPizza.getDescription());
        pizza.setImageUrl(updatedPizza.getImageUrl());
        pizza.setActive(updatedPizza.isActive());
        pizza.setIngredients(updatedPizza.getIngredients());
        pizza.setTags(updatedPizza.getTags());
        ensureUniqueSlug(pizza, id);
        return pizzaRepository.save(pizza);
    }

    public Pizza updateWithRelations(Long id, Pizza updatedPizza, List<Long> ingredientIds, List<Long> tagIds) {
        Pizza pizza = findById(id);
        pizza.setName(updatedPizza.getName());
        pizza.setDescription(updatedPizza.getDescription());
        pizza.setImageUrl(updatedPizza.getImageUrl());
        pizza.setActive(updatedPizza.isActive());

        // Vygenerovanie unikátneho slugu (ak sa zmenil názov)
        ensureUniqueSlug(pizza, id);

        // Nastavenie ingrediencií
        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            Set<Ingredient> ingredients = new HashSet<>(ingredientRepository.findAllById(ingredientIds));
            pizza.setIngredients(ingredients);
        } else {
            pizza.setIngredients(new HashSet<>());
        }

        // Nastavenie tagov
        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            pizza.setTags(tags);
        } else {
            pizza.setTags(new HashSet<>());
        }

        return pizzaRepository.save(pizza);
    }

    /**
     * Zabezpečí, že pizza má unikátny slug.
     * Ak slug už existuje (a nepatrí tej istej pizze), pridá číslo na koniec.
     */
    private void ensureUniqueSlug(Pizza pizza, Long currentPizzaId) {
        pizza.generateSlug();
        String baseSlug = pizza.getSlug();
        String slug = baseSlug;
        int counter = 1;
        
        while (true) {
            Optional<Pizza> existing = pizzaRepository.findBySlug(slug);
            if (existing.isEmpty()) {
                // Slug je unikátny
                break;
            }
            if (currentPizzaId != null && existing.get().getId().equals(currentPizzaId)) {
                // Slug patrí tej istej pizze (update)
                break;
            }
            // Slug už existuje, skúsime s číslom
            counter++;
            slug = baseSlug + "-" + counter;
        }
        
        pizza.setSlug(slug);
    }

    public void delete(Long id) {
        if (!pizzaRepository.existsById(id)) {
            throw new ObjectNotFoundException("Pizza s ID " + id + " neexistuje");
        }
        pizzaRepository.deleteById(id);
    }

    public void toggleActive(Long id) {
        Pizza pizza = findById(id);
        pizza.setActive(!pizza.isActive());
        pizzaRepository.save(pizza);
    }

    public PizzaSize findSizeById(Long sizeId) {
        return pizzaSizeRepository.findById(sizeId)
                .orElseThrow(() -> new ObjectNotFoundException("Veľkosť pizze s ID " + sizeId + " neexistuje"));
    }

    public void addSize(Long pizzaId, PizzaSize size) {
        Pizza pizza = findById(pizzaId);
        size.setPizza(pizza);
        pizzaSizeRepository.save(size);
    }

    public void removeSize(Long pizzaId, Long sizeId) {
        PizzaSize size = findSizeById(sizeId);
        if (!size.getPizza().getId().equals(pizzaId)) {
            throw new ObjectNotFoundException("Veľkosť nepatrí tejto pizze");
        }
        pizzaSizeRepository.delete(size);
    }
}
