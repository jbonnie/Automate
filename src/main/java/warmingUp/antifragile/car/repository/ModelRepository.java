package warmingUp.antifragile.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.car.domain.Model;

import java.util.ArrayList;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByName(String modelName);
    ArrayList<Model> findByPriceBetween(Integer minPrice, Integer maxPrice);
}
