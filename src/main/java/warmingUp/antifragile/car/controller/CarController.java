package warmingUp.antifragile.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.car.repository.ModelRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@RestController
public class CarController {

    @Autowired
    private ModelRepository modelRepository;

    // 전체 모델 랭킹 반환 요청 처리 (키워드 점수 높은 순 - 리뷰 많은 순 - 모델명 순)
    @GetMapping("/reviews/ranking")
    public ArrayList<Model> getAllRanking(@RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                          @RequestParam(value = "maxPrice", required = false) Integer maxPrice) {

        if(minPrice == null)
            minPrice = 0;
        if(maxPrice == null)
            maxPrice = Integer.MAX_VALUE;
        // 해당 가격 범위의 모델 목록 가져오기
        ArrayList<Model> list = modelRepository.findByPriceBetween(minPrice, maxPrice);

        // 만약 키워드 필터링이 적용되지 않았을 경우 -> 총 5개의 우선 고려사항 평균 점수 합산 높은 순 - 리뷰 많은 순 - 모델명 순 정렬
        if(keyword == null) {
            return noKeywordSort(list);
        }
        // 키워드 필터링 적용 -> 키워드 점수 높은 순 - 리뷰 많은 순 - 모델명 순
        return keywordSort(keyword, list);
    }

    // 키워드 필터가 적용되지 않음 -> 전체 키워드 평균 합산 높은 순 - 리뷰 많은 순 - 이름 순으로 정렬
    public ArrayList<Model> noKeywordSort(ArrayList<Model> list) {
        Collections.sort(list, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                // 1. 키워드 점수 높은 순
                if(m1.allKeywordAvg() > m2.allKeywordAvg())
                    return -1;
                else if(m1.allKeywordAvg() < m2.allKeywordAvg())
                    return 1;
                else {      // 2. 리뷰 많은 순
                    if(m1.getReviewCount() > m2.getReviewCount())
                        return -1;
                    else if(m1.getReviewCount() < m2.getReviewCount())
                        return 1;
                    else {      // 3. 이름 순
                        return m1.getName().compareTo(m2.getName());
                    }
                }
            }
        });
        return list;
    }
    // 정해진 키워드 기준으로 점수 높은 순 - 리뷰 많은 순 - 이름 순 정렬
    public ArrayList<Model> keywordSort(String keyword, ArrayList<Model> list) {
        Collections.sort(list, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                // 1. 키워드 점수 높은 순
                if(m1.keywordAvg(keyword) > m2.keywordAvg(keyword))
                    return -1;
                else if(m1.keywordAvg(keyword) < m2.keywordAvg(keyword))
                    return 1;
                else {      // 2. 리뷰 많은 순
                    if(m1.getReviewCount() > m2.getReviewCount())
                        return -1;
                    else if(m1.getReviewCount() < m2.getReviewCount())
                        return 1;
                    else {      // 3. 이름 순
                        return m1.getName().compareTo(m2.getName());
                    }
                }
            }
        });
        return list;
    }

    // 해당 모델의 상세 설명을 볼 수 있는 URL 링크 반환
    @GetMapping("/recommend/{modelId}")
    public String getURL(@PathVariable Long modelId) {
        Model model = modelRepository.findById(modelId).orElse(null);
        if(model == null)
            return "해당하는 모델이 없습니다.";
        else
            return model.getInformationURL();
    }
}
