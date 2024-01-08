package warmingUp.antifragile.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.car.domain.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
