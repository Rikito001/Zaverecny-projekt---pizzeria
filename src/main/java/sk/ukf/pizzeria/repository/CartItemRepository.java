package sk.ukf.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.pizzeria.entity.CartItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    Optional<CartItem> findByUserIdAndPizzaSizeId(Long userId, Long pizzaSizeId);

    void deleteByUserId(Long userId);

    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.user.id = :userId")
    Integer countByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(c.pizzaSize.price * c.quantity), 0) FROM CartItem c WHERE c.user.id = :userId")
    BigDecimal calculateTotalByUserId(@Param("userId") Long userId);
}
