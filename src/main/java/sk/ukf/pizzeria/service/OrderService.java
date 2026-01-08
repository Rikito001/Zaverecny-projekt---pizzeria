package sk.ukf.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.ukf.pizzeria.entity.*;
import sk.ukf.pizzeria.entity.enums.OrderStatus;
import sk.ukf.pizzeria.exception.ObjectNotFoundException;
import sk.ukf.pizzeria.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Objednavka s ID " + id + " neexistuje"));
    }

    public List<Order> findByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<Order> findByUserPaginated(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status);
    }

    public List<Order> findPendingAndPreparing() {
        return orderRepository.findByStatusIn(
                Arrays.asList(OrderStatus.CAKAJUCA, OrderStatus.PRIPRAVUJE_SA));
    }

    public List<Order> findReadyAndDelivering() {
        return orderRepository.findByStatusIn(
                Arrays.asList(OrderStatus.HOTOVA, OrderStatus.DORUCUJE_SA));
    }

    public List<Order> findActiveOrders() {
        return orderRepository.findActiveOrders();
    }

    public Long countPendingOrders() {
        return orderRepository.countPendingOrders();
    }

    public Order createOrder(User user, String deliveryAddress, String deliveryCity, 
                             String deliveryPhone, String note) {
        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Kosik je prazdny");
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryCity(deliveryCity);
        order.setDeliveryPhone(deliveryPhone);
        order.setNote(note);
        order.setStatus(OrderStatus.CAKAJUCA);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (CartItem cartItem : cartItems) {
            PizzaSize pizzaSize = cartItem.getPizzaSize();
            Pizza pizza = pizzaSize.getPizza();
            
            OrderItem orderItem = new OrderItem(
                    pizza.getName(),
                    pizzaSize.getSizeName(),
                    pizzaSize.getPrice(),
                    cartItem.getQuantity(),
                    pizza.getIngredientsAsString()
            );
            
            order.addItem(orderItem);
            total = total.add(orderItem.getTotalPrice());
        }
        
        order.setTotalPrice(total);
        Order savedOrder = orderRepository.save(order);
        
        cartService.clearCart(user.getId());
        
        return savedOrder;
    }

    public void changeStatus(Long orderId, OrderStatus newStatus) {
        Order order = findById(orderId);
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    public void acceptOrder(Long orderId, User cook) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.CAKAJUCA) {
            throw new IllegalStateException("Objednavka nie je v stave Cakajuca");
        }
        order.setStatus(OrderStatus.PRIPRAVUJE_SA);
        order.setCook(cook);
        orderRepository.save(order);
    }

    public void markAsReady(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.PRIPRAVUJE_SA) {
            throw new IllegalStateException("Objednavka nie je v stave Pripravuje sa");
        }
        order.setStatus(OrderStatus.HOTOVA);
        orderRepository.save(order);
    }

    public void pickupOrder(Long orderId, User courier) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.HOTOVA) {
            throw new IllegalStateException("Objednavka nie je v stave Hotova");
        }
        order.setStatus(OrderStatus.DORUCUJE_SA);
        order.setCourier(courier);
        orderRepository.save(order);
    }

    public void markAsDelivered(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.DORUCUJE_SA) {
            throw new IllegalStateException("Objednavka nie je v stave Dorucuje sa");
        }
        order.setStatus(OrderStatus.DORUCENA);
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId, Long userId) {
        Order order = findById(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new ObjectNotFoundException("Objednavka neexistuje");
        }
        if (!order.getStatus().isCancellable()) {
            throw new IllegalStateException("Objednavku nie je mozne zrusit");
        }
        order.setStatus(OrderStatus.ZRUSENA);
        orderRepository.save(order);
    }

    public void adminCancelOrder(Long orderId) {
        Order order = findById(orderId);
        order.setStatus(OrderStatus.ZRUSENA);
        orderRepository.save(order);
    }
}
