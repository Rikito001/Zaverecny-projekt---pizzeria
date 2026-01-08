package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.Ingredient;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.IngredientRepository;
import java.util.List;

@Service
@Transactional
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public List<Ingredient> findAllAvailable() {
        return ingredientRepository.findByAvailableTrue();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Ingrediencia s ID " + id + " neexistuje"));
    }

    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient update(Long id, Ingredient updatedIngredient) {
        Ingredient ingredient = findById(id);
        ingredient.setName(updatedIngredient.getName());
        ingredient.setDescription(updatedIngredient.getDescription());
        ingredient.setPriceExtra(updatedIngredient.getPriceExtra());
        ingredient.setAllergens(updatedIngredient.getAllergens());
        ingredient.setAvailable(updatedIngredient.isAvailable());
        return ingredientRepository.save(ingredient);
    }

    public void delete(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new ObjectNotFoundException("Ingrediencia s ID " + id + " neexistuje");
        }
        ingredientRepository.deleteById(id);
    }

    public void toggleAvailable(Long id) {
        Ingredient ingredient = findById(id);
        ingredient.setAvailable(!ingredient.isAvailable());
        ingredientRepository.save(ingredient);
    }

    public boolean existsByName(String name) {
        return ingredientRepository.existsByName(name);
    }
}
