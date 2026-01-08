package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.CartItem;
import sk.ukf.pizzeria.entity.PizzaSize;
import sk.ukf.pizzeria.entity.User;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.CartItemRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final PizzaService pizzaService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, PizzaService pizzaService) {
        this.cartItemRepository = cartItemRepository;
        this.pizzaService = pizzaService;
    }

    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void addToCart(User user, Long pizzaSizeId, Integer quantity) {
        PizzaSize pizzaSize = pizzaService.findSizeById(pizzaSizeId);
        
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndPizzaSizeId(user.getId(), pizzaSizeId);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(user, pizzaSize, quantity);
            cartItemRepository.save(newItem);
        }
    }

    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ObjectNotFoundException("Polozka kosika neexistuje"));
        
        if (!item.getUser().getId().equals(userId)) {
            throw new ObjectNotFoundException("Polozka kosika neexistuje");
        }
        
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeFromCart(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ObjectNotFoundException("Polozka kosika neexistuje"));
        
        if (!item.getUser().getId().equals(userId)) {
            throw new ObjectNotFoundException("Polozka kosika neexistuje");
        }
        
        cartItemRepository.delete(item);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public Integer getCartItemCount(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }

    public BigDecimal getCartTotal(Long userId) {
        return cartItemRepository.calculateTotalByUserId(userId);
    }

    public boolean isCartEmpty(Long userId) {
        return getCartItemCount(userId) == 0;
    }
}
