package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pizza_sizes")
public class PizzaSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    @NotBlank(message = "Nazov velkosti nesmie byt prazdny")
    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;

    @NotNull(message = "Cena je povinna")
    @DecimalMin(value = "0.01", message = "Cena musi byt vacsia ako 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Min(value = 20, message = "Priemer musi byt aspon 20 cm")
    @Max(value = 60, message = "Priemer moze byt najviac 60 cm")
    @Column(name = "diameter_cm")
    private Integer diameterCm;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public PizzaSize() {}

    public PizzaSize(String sizeName, BigDecimal price, Integer diameterCm) {
        this.sizeName = sizeName;
        this.price = price;
        this.diameterCm = diameterCm;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }
    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getDiameterCm() { return diameterCm; }
    public void setDiameterCm(Integer diameterCm) { this.diameterCm = diameterCm; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
