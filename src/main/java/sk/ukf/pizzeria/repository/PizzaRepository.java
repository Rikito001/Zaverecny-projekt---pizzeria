package sk.ukf.pizzeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.pizzeria.entity.Pizza;
import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    // 1. Vyhladavanie podla slug-u (pre SEO URL)
    Optional<Pizza> findBySlug(String slug);

    // 2. SEARCHBAR - vyhladavanie podla nazvu/popisu
    @Query("SELECT p FROM Pizza p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Pizza> searchByKeyword(@Param("keyword") String keyword);

    // 3. Filtrovanie podla tagu
    @Query("SELECT DISTINCT p FROM Pizza p JOIN p.tags t WHERE t.id = :tagId AND p.active = true")
    List<Pizza> findByTagId(@Param("tagId") Long tagId);

    // 4. Vsetky aktivne pizze
    List<Pizza> findByActiveTrue();

    // 5. Strankovanie aktivnych pizz (BONUS)
    Page<Pizza> findByActiveTrue(Pageable pageable);

    // 6. Kontrola existencie slug-u
    boolean existsBySlug(String slug);

    // 7. Strankovanie s vyhladavanim
    @Query("SELECT p FROM Pizza p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Pizza> searchByKeywordPaginated(@Param("keyword") String keyword, Pageable pageable);
}
