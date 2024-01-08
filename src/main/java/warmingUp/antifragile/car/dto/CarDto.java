package warmingUp.antifragile.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    private Long id;
    private String carNum;
    private String modelName;
    private Integer carAge;
    private Integer buyYear;
    private Integer buyMonth;
}
