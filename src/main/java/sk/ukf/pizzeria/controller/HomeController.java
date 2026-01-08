package sk.ukf.pizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.ukf.pizzeria.service.PizzaService;
import sk.ukf.pizzeria.service.TagService;

@Controller
public class HomeController {

    private final PizzaService pizzaService;
    private final TagService tagService;

    @Autowired
    public HomeController(PizzaService pizzaService, TagService tagService) {
        this.pizzaService = pizzaService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String home(Model model, 
                       @RequestParam(required = false) String odhlaseny) {
        model.addAttribute("pizzas", pizzaService.findAllActive());
        model.addAttribute("tags", tagService.findAll());
        
        if (odhlaseny != null) {
            model.addAttribute("message", "Boli ste uspesne odhlaseny");
        }
        
        return "home";
    }
}
