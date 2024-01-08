package warmingUp.antifragile.car.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String carNum;
    @Column(nullable = false)
    private Long modelId;
    @Column
    private Integer carAge;
    @Column
    private Integer buyYear;
    @Column
    private Integer buyMonth;
}
