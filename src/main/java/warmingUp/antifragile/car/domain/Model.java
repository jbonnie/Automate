package warmingUp.antifragile.car.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String name;                // 모델명
    @Column(nullable = false)
    private String type;                // 차종 (ex. SUV...)
    @Column(nullable = false)
    private String description;         // 차량 캐치프레이즈
    @Column(nullable = false)
    private String informationURL;      // 차량 상세보기 링크
    @Column(nullable = false)
    private Integer people;         // 탑승 가능 인원수
    @Column(nullable = false)
    private Integer price;          // 가격

    @ColumnDefault("0L")
    private Long reviewCount;       // 해당 모델의 리뷰 게시물 총 개수

    @ColumnDefault("0L")
    private Long mpgSum;            // 누적 연비 점수
    @ColumnDefault("0L")
    private Long safeSum;           // 누적 승차감 및 안전 점수
    @ColumnDefault("0L")
    private Long spaceSum;          // 누적 넓은 공간 점수
    @ColumnDefault("0L")
    private Long designSum;         // 누적 디자인 점수
    @ColumnDefault("0L")
    private Long funSum;            // 누적 운전 재미 점수

    @ColumnDefault("0L")
    private Long workCount;         // 누적 출퇴근 목적 개수
    @ColumnDefault("0L")
    private Long longCount;         // 누적 장거리 운전 목적 개수
    @ColumnDefault("0L")
    private Long driveCount;        // 누적 드라이브 목적 개수
    @ColumnDefault("0L")
    private Long travelCount;       // 누적 주말여행 목적 개수
    @ColumnDefault("0L")
    private Long kidsCount;         // 누적 자녀와 함께 목적 개수
}
