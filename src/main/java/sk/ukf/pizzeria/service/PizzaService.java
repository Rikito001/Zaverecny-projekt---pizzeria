package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.Pizza;
import sk.ukf.pizzeria.entity.PizzaSize;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.PizzaRepository;
import sk.ukf.pizzeria.repository.PizzaSizeRepository;
import java.util.List;

@Service
@Transactional
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaSizeRepository pizzaSizeRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaSizeRepository pizzaSizeRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaSizeRepository = pizzaSizeRepository;
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
        return pizzaRepository.save(pizza);
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
                .orElseThrow(() -> new ObjectNotFoundException("Velkost pizze s ID " + sizeId + " neexistuje"));
    }

    public void addSize(Long pizzaId, PizzaSize size) {
        Pizza pizza = findById(pizzaId);
        pizza.addSize(size);
        pizzaRepository.save(pizza);
    }

    public void removeSize(Long pizzaId, Long sizeId) {
        Pizza pizza = findById(pizzaId);
        PizzaSize size = findSizeById(sizeId);
        pizza.removeSize(size);
        pizzaRepository.save(pizza);
    }
}
