package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazov ingrediencie nesmie byt prazdny")
    @Size(min = 2, max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    private String description;

    @DecimalMin(value = "0.0")
    @Column(name = "price_extra", precision = 10, scale = 2)
    private BigDecimal priceExtra = BigDecimal.ZERO;

    @Size(max = 255)
    private String allergens;

    @Column(nullable = false)
    private boolean available = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Pizza> pizzas = new HashSet<>();

    public Ingredient() {}

    public Ingredient(String name, String allergens) {
        this.name = name;
        this.allergens = allergens;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPriceExtra() { return priceExtra; }
    public void setPriceExtra(BigDecimal priceExtra) { this.priceExtra = priceExtra; }
    public String getAllergens() { return allergens; }
    public void setAllergens(String allergens) { this.allergens = allergens; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Set<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(Set<Pizza> pizzas) { this.pizzas = pizzas; }
}
