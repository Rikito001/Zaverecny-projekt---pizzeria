package sk.ukf.pizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.ukf.pizzeria.entity.Pizza;
import sk.ukf.pizzeria.entity.PizzaSize;
import sk.ukf.pizzeria.service.IngredientService;
import sk.ukf.pizzeria.service.PizzaService;
import sk.ukf.pizzeria.service.TagService;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/pizza")
public class PizzaController {

    private final PizzaService pizzaService;
    private final TagService tagService;
    private final IngredientService ingredientService;

    @Autowired
    public PizzaController(PizzaService pizzaService, TagService tagService, 
                          IngredientService ingredientService) {
        this.pizzaService = pizzaService;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String listPizzas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long tagId,
            Model model) {

        if (search != null && !search.isEmpty()) {
            model.addAttribute("pizzas", pizzaService.search(search));
            model.addAttribute("searchTerm", search);
        } else if (tagId != null) {
            model.addAttribute("pizzas", pizzaService.findByTag(tagId));
            model.addAttribute("selectedTag", tagId);
        } else {
            Page<Pizza> pizzaPage = pizzaService.findAllActivePaginated(PageRequest.of(page, size));
            model.addAttribute("pizzas", pizzaPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pizzaPage.getTotalPages());
        }

        model.addAttribute("tags", tagService.findAll());
        return "pizza/list";
    }

    @GetMapping("/{slug}")
    public String viewPizza(@PathVariable String slug, Model model) {
        Pizza pizza = pizzaService.findBySlug(slug);
        model.addAttribute("pizza", pizza);
        return "pizza/detail";
    }

    @GetMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "pizza/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String savePizza(@Valid @ModelAttribute("pizza") Pizza pizza,
                           BindingResult bindingResult,
                           @RequestParam(required = false) List<Long> ingredientIds,
                           @RequestParam(required = false) List<Long> tagIds,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "pizza/form";
        }

        Pizza savedPizza = pizzaService.saveWithRelations(pizza, ingredientIds, tagIds);
        redirectAttributes.addFlashAttribute("message", "Pizza bola úspešne vytvorená. Teraz môžete pridať veľkosti.");
        return "redirect:/pizza/" + savedPizza.getId() + "/upravit";
    }

    @GetMapping("/{id}/upravit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("pizza", pizzaService.findById(id));
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "pizza/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePizza(@PathVariable Long id,
                             @Valid @ModelAttribute("pizza") Pizza pizza,
                             BindingResult bindingResult,
                             @RequestParam(required = false) List<Long> ingredientIds,
                             @RequestParam(required = false) List<Long> tagIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "pizza/form";
        }

        pizzaService.updateWithRelations(id, pizza, ingredientIds, tagIds);
        redirectAttributes.addFlashAttribute("message", "Pizza bola úspešne aktualizovaná");
        return "redirect:/pizza/" + id + "/upravit";
    }

    @PostMapping("/{id}/vymazat")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePizza(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pizzaService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Pizza bola úspešne vymazaná");
        return "redirect:/pizza";
    }

    @PostMapping("/{id}/velkost")
    @PreAuthorize("hasRole('ADMIN')")
    public String addSize(@PathVariable Long id,
                         @RequestParam String sizeName,
                         @RequestParam BigDecimal price,
                         @RequestParam(required = false) Integer diameterCm,
                         RedirectAttributes redirectAttributes) {
        PizzaSize size = new PizzaSize(sizeName, price, diameterCm);
        pizzaService.addSize(id, size);
        redirectAttributes.addFlashAttribute("message", "Veľkosť bola pridaná");
        return "redirect:/pizza/" + id + "/upravit";
    }

    @PostMapping("/{id}/velkost/{sizeId}/vymazat")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeSize(@PathVariable Long id,
                            @PathVariable Long sizeId,
                            RedirectAttributes redirectAttributes) {
        pizzaService.removeSize(id, sizeId);
        redirectAttributes.addFlashAttribute("message", "Veľkosť bola odstránená");
        return "redirect:/pizza/" + id + "/upravit";
    }
}
