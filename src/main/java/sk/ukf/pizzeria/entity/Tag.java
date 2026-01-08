package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazov tagu nesmie byt prazdny")
    @Size(min = 2, max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 7)
    private String color = "#6c757d";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "tags")
    private Set<Pizza> pizzas = new HashSet<>();

    public Tag() {}

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(Set<Pizza> pizzas) { this.pizzas = pizzas; }
}
