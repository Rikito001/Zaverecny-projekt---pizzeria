package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_size_id", nullable = false)
    private PizzaSize pizzaSize;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public CartItem() {}

    public CartItem(User user, PizzaSize pizzaSize, Integer quantity) {
        this.user = user;
        this.pizzaSize = pizzaSize;
        this.quantity = quantity;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public BigDecimal getTotalPrice() {
        return pizzaSize.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public PizzaSize getPizzaSize() { return pizzaSize; }
    public void setPizzaSize(PizzaSize pizzaSize) { this.pizzaSize = pizzaSize; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
