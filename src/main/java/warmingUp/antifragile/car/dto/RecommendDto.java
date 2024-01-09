package warmingUp.antifragile.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendDto {

    private Integer adultsCount;
    private Integer kidsCount;
    private Integer minPrice;
    private Integer maxPrice;
    private String priority1;
    private String priority2;
    private String purpose;
}
