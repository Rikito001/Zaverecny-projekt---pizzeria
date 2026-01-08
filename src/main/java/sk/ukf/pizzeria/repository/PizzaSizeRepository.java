package sk.ukf.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.ukf.pizzeria.entity.PizzaSize;
import java.util.List;

@Repository
public interface PizzaSizeRepository extends JpaRepository<PizzaSize, Long> {

    List<PizzaSize> findByPizzaId(Long pizzaId);

    void deleteByPizzaId(Long pizzaId);
}
