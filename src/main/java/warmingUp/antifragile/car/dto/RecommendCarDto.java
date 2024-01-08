package warmingUp.antifragile.car.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendCarDto {

    private Long modelId;
    private String name;
    private String description;
    private String informationURL;
    private Integer price;
    private String priority;        // 우선 고려사항 중 1위 항목
    private Integer priorityScore;      // 1위 항목의 퍼센트 비율
    private String purpose;             // 주요 목적 중 1위 항목
    private Integer purposeScore;       // 1위 항목의 퍼센트 비율
}
