package sk.ukf.pizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.ukf.pizzeria.entity.Ingredient;
import sk.ukf.pizzeria.entity.Tag;
import sk.ukf.pizzeria.entity.enums.OrderStatus;
import sk.ukf.pizzeria.entity.enums.Role;
import sk.ukf.pizzeria.service.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final PizzaService pizzaService;
    private final OrderService orderService;
    private final TagService tagService;
    private final IngredientService ingredientService;

    @Autowired
    public AdminController(UserService userService, PizzaService pizzaService,
                          OrderService orderService, TagService tagService,
                          IngredientService ingredientService) {
        this.userService = userService;
        this.pizzaService = pizzaService;
        this.orderService = orderService;
        this.tagService = tagService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pendingOrdersCount", orderService.countPendingOrders());
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("totalPizzas", pizzaService.findAll().size());
        model.addAttribute("activeOrders", orderService.findActiveOrders().size());
        return "admin/dashboard";
    }

    // === POUZIVATELIA ===

    @GetMapping("/pouzivatelia")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @PostMapping("/pouzivatelia/{id}/rola")
    public String updateUserRole(@PathVariable Long id,
                                @RequestParam Role role,
                                RedirectAttributes redirectAttributes) {
        userService.updateRole(id, role);
        redirectAttributes.addFlashAttribute("message", "Rola bola aktualizovana");
        return "redirect:/admin/pouzivatelia";
    }

    @PostMapping("/pouzivatelia/{id}/aktivovat")
    public String toggleUserActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleActive(id);
        redirectAttributes.addFlashAttribute("message", "Stav pouzivatela bol zmeneny");
        return "redirect:/admin/pouzivatelia";
    }

    // === OBJEDNAVKY ===

    @GetMapping("/objednavky")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/objednavky/{id}/stav")
    public String updateOrderStatus(@PathVariable Long id,
                                   @RequestParam OrderStatus status,
                                   RedirectAttributes redirectAttributes) {
        orderService.changeStatus(id, status);
        redirectAttributes.addFlashAttribute("message", "Stav objednavky bol zmeneny");
        return "redirect:/admin/objednavky";
    }

    @PostMapping("/objednavky/{id}/zrusit")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        orderService.adminCancelOrder(id);
        redirectAttributes.addFlashAttribute("message", "Objednavka bola zrusena");
        return "redirect:/admin/objednavky";
    }

    // === TAGY ===

    @GetMapping("/tagy")
    public String tags(Model model) {
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("tag", new Tag());
        return "admin/tags";
    }

    @PostMapping("/tagy")
    public String saveTag(@Valid @ModelAttribute("tag") Tag tag,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tags", tagService.findAll());
            return "admin/tags";
        }
        tagService.save(tag);
        redirectAttributes.addFlashAttribute("message", "Tag bol ulozeny");
        return "redirect:/admin/tagy";
    }

    @PostMapping("/tagy/{id}/vymazat")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tagService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Tag bol vymazany");
        return "redirect:/admin/tagy";
    }

    // === INGREDIENCIE ===

    @GetMapping("/ingrediencie")
    public String ingredients(Model model) {
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("ingredient", new Ingredient());
        return "admin/ingredients";
    }

    @PostMapping("/ingrediencie")
    public String saveIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredients", ingredientService.findAll());
            return "admin/ingredients";
        }
        ingredientService.save(ingredient);
        redirectAttributes.addFlashAttribute("message", "Ingrediencia bola ulozena");
        return "redirect:/admin/ingrediencie";
    }

    @PostMapping("/ingrediencie/{id}/vymazat")
    public String deleteIngredient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ingredientService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Ingrediencia bola vymazana");
        return "redirect:/admin/ingrediencie";
    }

    @PostMapping("/ingrediencie/{id}/dostupnost")
    public String toggleIngredientAvailable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ingredientService.toggleAvailable(id);
        redirectAttributes.addFlashAttribute("message", "Dostupnost bola zmenena");
        return "redirect:/admin/ingrediencie";
    }

    // === PIZZE ===

    @GetMapping("/pizze")
    public String pizzas(Model model) {
        model.addAttribute("pizzas", pizzaService.findAll());
        return "admin/pizzas";
    }

    @PostMapping("/pizze/{id}/aktivovat")
    public String togglePizzaActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pizzaService.toggleActive(id);
        redirectAttributes.addFlashAttribute("message", "Stav pizze bol zmeneny");
        return "redirect:/admin/pizze";
    }
}
