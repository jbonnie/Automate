package warmingUp.antifragile.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.car.repository.ModelRepository;

import java.util.ArrayList;

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

        }
        // 키워드 필터링 적용 -> 키워드 점수 높은 순 - 리뷰 많은 순 - 모델명 순
        return list;
    }
}
