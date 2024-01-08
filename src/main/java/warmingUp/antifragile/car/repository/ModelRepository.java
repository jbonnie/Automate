package warmingUp.antifragile.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.car.domain.Model;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByName(String modelName);
}
