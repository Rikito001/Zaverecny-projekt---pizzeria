package sk.ukf.pizzeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.pizzeria.entity.Order;
import sk.ukf.pizzeria.entity.enums.OrderStatus;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Objednavky pouzivatela
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Strankovanie objednavok pouzivatela
    Page<Order> findByUserId(Long userId, Pageable pageable);

    // Objednavky podla stavu
    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    // Pre kuchara (cakajuce + pripravovane)
    @Query("SELECT o FROM Order o WHERE o.status IN :statuses ORDER BY o.createdAt ASC")
    List<Order> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    // Pocet novych objednavok (pre notifikaciu)
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CAKAJUCA'")
    Long countPendingOrders();

    // Objednavky kuchara
    List<Order> findByCookIdOrderByCreatedAtDesc(Long cookId);

    // Objednavky kuriera
    List<Order> findByCourierIdOrderByCreatedAtDesc(Long courierId);

    // Aktivne objednavky
    @Query("SELECT o FROM Order o WHERE o.status NOT IN ('DORUCENA', 'ZRUSENA') ORDER BY o.createdAt ASC")
    List<Order> findActiveOrders();
}
