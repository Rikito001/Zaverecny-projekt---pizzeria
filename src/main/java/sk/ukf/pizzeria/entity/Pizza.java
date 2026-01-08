package sk.ukf.pizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pizzas")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazov pizze nesmie byt prazdny")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 120)
    private String slug;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("price ASC")
    private List<PizzaSize> sizes = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "pizza_ingredients",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "pizza_tags",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Pizza() {}

    public Pizza(String name, String description) {
        this.name = name;
        this.description = description;
        generateSlug();
    }

    @PrePersist
    protected void onCreate() {
        generateSlug();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        generateSlug();
        updatedAt = LocalDateTime.now();
    }

    public void generateSlug() {
        if (name != null && !name.isEmpty()) {
            slug = name.toLowerCase()
                    .replaceAll("[áäâ]", "a")
                    .replaceAll("[éěê]", "e")
                    .replaceAll("[íîý]", "i")
                    .replaceAll("[óôö]", "o")
                    .replaceAll("[úůü]", "u")
                    .replaceAll("č", "c").replaceAll("ď", "d")
                    .replaceAll("ľ", "l").replaceAll("ň", "n")
                    .replaceAll("ř", "r").replaceAll("š", "s")
                    .replaceAll("ť", "t").replaceAll("ž", "z")
                    .replaceAll("[^a-z0-9]", "-")
                    .replaceAll("-+", "-")
                    .replaceAll("^-|-$", "");
        }
    }

    public void addSize(PizzaSize size) {
        sizes.add(size);
        size.setPizza(this);
    }

    public void removeSize(PizzaSize size) {
        sizes.remove(size);
        size.setPizza(null);
    }

    public BigDecimal getLowestPrice() {
        return sizes.stream()
                .map(PizzaSize::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public String getIngredientsAsString() {
        return ingredients.stream()
                .map(Ingredient::getName)
                .sorted()
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<PizzaSize> getSizes() { return sizes; }
    public void setSizes(List<PizzaSize> sizes) { this.sizes = sizes; }
    public Set<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(Set<Ingredient> ingredients) { this.ingredients = ingredients; }
    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }
}
