package warmingUp.antifragile.car.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    // 해당 모델의 총 키워드 평균 점수 구하기
    public double allKeywordAvg() {
        return (mpgSum.doubleValue() + safeSum.doubleValue()
                + spaceSum.doubleValue() + designSum.doubleValue()
                + funSum.doubleValue()) / reviewCount;
    }
    // 해당 모델의 키워드별 평균 점수 반환
    public double keywordAvg(String keyword) {
        double result = 0.0;
        switch(keyword) {
            case "연비":
                result = mpgSum.doubleValue() / reviewCount;
                break;
            case "승차감 및 안전":
                result = safeSum.doubleValue() / reviewCount;
                break;
            case "넓은 공간":
                result = spaceSum.doubleValue() / reviewCount;
                break;
            case "디자인":
                result = designSum.doubleValue() / reviewCount;
                break;
            case "운전 재미":
                result = funSum.doubleValue() / reviewCount;
                break;
            case "출퇴근용":
                result = workCount.doubleValue() / reviewCount;
                break;
            case "장거리 운전":
                result = longCount.doubleValue() / reviewCount;
                break;
            case "드라이브":
                result = driveCount.doubleValue() / reviewCount;
                break;
            case "주말여행":
                result = travelCount.doubleValue() / reviewCount;
                break;
            case "자녀와 함께":
                result = kidsCount.doubleValue() / reviewCount;
                break;
        }
        return result;
    }

    // 두 개의 우선 고려 사항 평균 점수 합산하여 반환
    public double twoPriorityScore(String p1, String p2) {
        double score = 0.0;
        if(p1.equals("연비") || p2.equals("연비"))
            score += mpgSum.doubleValue() / reviewCount;
        if(p1.equals("승차감 및 안전") || p2.equals("승차감 및 안전"))
            score += mpgSum.doubleValue() / reviewCount;
        if(p1.equals("넓은 공간") || p2.equals("넓은 공간"))
            score += mpgSum.doubleValue() / reviewCount;
        if(p1.equals("디자인") || p2.equals("디자인"))
            score += mpgSum.doubleValue() / reviewCount;
        if(p1.equals("운전 재미") || p2.equals("운전 재미"))
            score += mpgSum.doubleValue() / reviewCount;
        return score;
    }

    // 해당 주요 이용 목적이 전체 리뷰에서 가지는 퍼센트 비율 반환
    public double purposePercent(String purpose) {
        double percent = 0.0;
        switch(purpose) {
            case "출퇴근용":
                percent = workCount.doubleValue() / reviewCount * 100;
                break;
            case "장거리 운전":
                percent = longCount.doubleValue() / reviewCount * 100;
                break;
            case "드라이브":
                percent = driveCount.doubleValue() / reviewCount * 100;
                break;
            case "주말여행":
                percent = travelCount.doubleValue() / reviewCount * 100;
                break;
            case "자녀와 함께":
                percent = kidsCount.doubleValue() / reviewCount * 100;
                break;
        }
        return percent;
    }

    // 해당 우선 고려 사항이 전체 리뷰에서 가지는 퍼센트 비율 반환
    public double priorityPercent(String priority) {
        double percent = 0.0;
        double sum = mpgSum.doubleValue() + safeSum.doubleValue()
                + spaceSum.doubleValue() + designSum.doubleValue() + funSum.doubleValue();
        switch(priority) {
            case "연비":
                percent = mpgSum.doubleValue() / sum * 100;
                break;
            case "승차감 및 안전":
                percent = safeSum.doubleValue() / sum * 100;
                break;
            case "넓은 공간":
                percent = spaceSum.doubleValue() / sum * 100;
                break;
            case "디자인":
                percent = designSum.doubleValue() / sum * 100;
                break;
            case "운전 재미":
                percent = funSum.doubleValue() / sum * 100;
                break;
        }
        return percent;
    }

    // 해당 모델에서 우선 고려사항 중 퍼센트 비율이 가장 높은 것 이름 반환
    public String bestPriority() {

        double[] score = new double[5];
        score[0] = priorityPercent("연비");
        score[1] = priorityPercent("승차감 및 안전");
        score[2] = priorityPercent("넓은 공간");
        score[3] = priorityPercent("디자인");
        score[4] = priorityPercent("운전 재미");

        int index = 0;
        double max = score[0];

        for(int i = 1; i < score.length; i++) {
            if(score[i] > max) {
                index = i;
                max = score[i];
            }
        }

        String result = null;
        switch(index) {
            case 0:
                result = "연비";
                break;
            case 1:
                result = "승차감 및 안전";
                break;
            case 2:
                result = "넓은 공간";
                break;
            case 3:
                result = "디자인";
                break;
            case 4:
                result = "운전 재미";
                break;
        }
        return result;
    }

    // 해당 모델에서 주요 이용 목적 중 퍼센트 비율이 가장 높은 것 이름 반환
    public String bestPurpose() {

        double[] score = new double[5];
        score[0] = purposePercent("출퇴근용");
        score[1] = purposePercent("장거리 운전");
        score[2] = purposePercent("드라이브");
        score[3] = purposePercent("주말여행");
        score[4] = purposePercent("자녀와 함께");

        int index = 0;
        double max = score[0];

        for(int i = 1; i < score.length; i++) {
            if(score[i] > max) {
                index = i;
                max = score[i];
            }
        }

        String result = null;
        switch(index) {
            case 0:
                result = "출퇴근용";
                break;
            case 1:
                result = "장거리 운전";
                break;
            case 2:
                result = "드라이브";
                break;
            case 3:
                result = "주말여행";
                break;
            case 4:
                result = "자녀와 함께";
                break;
        }
        return result;
    }
}
