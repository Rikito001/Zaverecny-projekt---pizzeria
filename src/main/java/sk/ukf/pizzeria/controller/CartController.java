package sk.ukf.pizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.ukf.pizzeria.security.CustomUserDetails;
import sk.ukf.pizzeria.service.CartService;

@Controller
@RequestMapping("/kosik")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(userDetails.getId()));
        model.addAttribute("cartTotal", cartService.getCartTotal(userDetails.getId()));
        return "cart/view";
    }

    @PostMapping("/pridat")
    public String addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestParam Long pizzaSizeId,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           RedirectAttributes redirectAttributes) {
        cartService.addToCart(userDetails.getUser(), pizzaSizeId, quantity);
        redirectAttributes.addFlashAttribute("message", "Pizza bola pridana do kosika");
        return "redirect:/kosik";
    }

    @PostMapping("/aktualizovat/{id}")
    public String updateQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam Integer quantity,
                                RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(userDetails.getId(), id, quantity);
        redirectAttributes.addFlashAttribute("message", "Kosik bol aktualizovany");
        return "redirect:/kosik";
    }

    @PostMapping("/odstranit/{id}")
    public String removeFromCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(userDetails.getId(), id);
        redirectAttributes.addFlashAttribute("message", "Polozka bola odstranena z kosika");
        return "redirect:/kosik";
    }

    @PostMapping("/vyprazdnit")
    public String clearCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        cartService.clearCart(userDetails.getId());
        redirectAttributes.addFlashAttribute("message", "Kosik bol vyprazdneny");
        return "redirect:/kosik";
    }
}
