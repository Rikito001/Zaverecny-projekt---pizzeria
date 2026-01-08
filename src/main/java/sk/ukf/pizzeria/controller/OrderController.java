package sk.ukf.pizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.ukf.pizzeria.entity.Order;
import sk.ukf.pizzeria.entity.enums.OrderStatus;
import sk.ukf.pizzeria.security.CustomUserDetails;
import sk.ukf.pizzeria.service.CartService;
import sk.ukf.pizzeria.service.OrderService;
import sk.ukf.pizzeria.service.UserService;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    // === ZAKAZNIK ===

    @GetMapping("/objednavka/nova")
    public String newOrderForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (cartService.isCartEmpty(userDetails.getId())) {
            return "redirect:/kosik";
        }
        model.addAttribute("cartItems", cartService.getCartItems(userDetails.getId()));
        model.addAttribute("cartTotal", cartService.getCartTotal(userDetails.getId()));
        model.addAttribute("user", userService.findById(userDetails.getId()));
        return "order/checkout";
    }

    @PostMapping("/objednavka/vytvorit")
    public String createOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam String deliveryAddress,
                             @RequestParam String deliveryCity,
                             @RequestParam String deliveryPhone,
                             @RequestParam(required = false) String note,
                             RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.createOrder(
                    userDetails.getUser(),
                    deliveryAddress,
                    deliveryCity,
                    deliveryPhone,
                    note
            );
            redirectAttributes.addFlashAttribute("message", "Objednavka c. " + order.getId() + " bola vytvorena");
            return "redirect:/moje-objednavky/" + order.getId();
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/kosik";
        }
    }

    @GetMapping("/moje-objednavky")
    public String myOrders(@AuthenticationPrincipal CustomUserDetails userDetails,
                          @RequestParam(defaultValue = "0") int page,
                          Model model) {
        model.addAttribute("orders", orderService.findByUserPaginated(
                userDetails.getId(), PageRequest.of(page, 10)));
        model.addAttribute("currentPage", page);
        return "order/my-orders";
    }

    @GetMapping("/moje-objednavky/{id}")
    public String orderDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long id,
                             Model model) {
        Order order = orderService.findById(id);
        if (!order.getUser().getId().equals(userDetails.getId())) {
            return "redirect:/moje-objednavky";
        }
        model.addAttribute("order", order);
        return "order/detail";
    }

    @PostMapping("/moje-objednavky/{id}/zrusit")
    public String cancelOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, userDetails.getId());
            redirectAttributes.addFlashAttribute("message", "Objednavka bola zrusena");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/moje-objednavky/" + id;
    }

    // === KUCHYNA ===

    @GetMapping("/kuchyna")
    @PreAuthorize("hasAnyRole('KUCHAR', 'ADMIN')")
    public String kitchenView(Model model) {
        model.addAttribute("pendingOrders", orderService.findByStatus(OrderStatus.CAKAJUCA));
        model.addAttribute("preparingOrders", orderService.findByStatus(OrderStatus.PRIPRAVUJE_SA));
        model.addAttribute("newOrdersCount", orderService.countPendingOrders());
        return "order/kitchen";
    }

    @PostMapping("/kuchyna/{id}/prijat")
    @PreAuthorize("hasAnyRole('KUCHAR', 'ADMIN')")
    public String acceptOrder(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        try {
            orderService.acceptOrder(id, userDetails.getUser());
            redirectAttributes.addFlashAttribute("message", "Objednavka bola prijata");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/kuchyna";
    }

    @PostMapping("/kuchyna/{id}/hotova")
    @PreAuthorize("hasAnyRole('KUCHAR', 'ADMIN')")
    public String markReady(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.markAsReady(id);
            redirectAttributes.addFlashAttribute("message", "Objednavka je pripravena na vyzdvihnutie");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/kuchyna";
    }

    // === ROZVOZ ===

    @GetMapping("/rozvoz")
    @PreAuthorize("hasAnyRole('KURIER', 'ADMIN')")
    public String deliveryView(Model model) {
        model.addAttribute("readyOrders", orderService.findByStatus(OrderStatus.HOTOVA));
        model.addAttribute("deliveringOrders", orderService.findByStatus(OrderStatus.DORUCUJE_SA));
        return "order/delivery";
    }

    @PostMapping("/rozvoz/{id}/prevziat")
    @PreAuthorize("hasAnyRole('KURIER', 'ADMIN')")
    public String pickupOrder(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        try {
            orderService.pickupOrder(id, userDetails.getUser());
            redirectAttributes.addFlashAttribute("message", "Objednavka bola prevzata na dorucenie");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rozvoz";
    }

    @PostMapping("/rozvoz/{id}/dorucena")
    @PreAuthorize("hasAnyRole('KURIER', 'ADMIN')")
    public String markDelivered(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.markAsDelivered(id);
            redirectAttributes.addFlashAttribute("message", "Objednavka bola dorucena");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rozvoz";
    }
}
