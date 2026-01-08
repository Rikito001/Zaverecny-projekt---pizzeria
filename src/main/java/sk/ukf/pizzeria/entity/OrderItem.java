package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Historicke hodnoty - bez FK vazieb
    @NotBlank
    @Column(name = "pizza_name", nullable = false, length = 100)
    private String pizzaName;

    @NotBlank
    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;

    @NotNull
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 1000)
    private String ingredients;

    public OrderItem() {}

    public OrderItem(String pizzaName, String sizeName, BigDecimal unitPrice, Integer quantity, String ingredients) {
        this.pizzaName = pizzaName;
        this.sizeName = sizeName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.ingredients = ingredients;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public String getPizzaName() { return pizzaName; }
    public void setPizzaName(String pizzaName) { this.pizzaName = pizzaName; }
    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
}
