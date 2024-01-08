package warmingUp.antifragile.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.car.domain.Car;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findById(Long id);
}
