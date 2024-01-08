package warmingUp.antifragile.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.car.dto.RecommendCarDto;
import warmingUp.antifragile.car.dto.RecommendDto;
import warmingUp.antifragile.car.repository.ModelRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
    // 유저의 선택 기반 추천 모델 2가지 반환
    // 인원수 -> 예산 범위 -> 우선 고려 사항 점수 -> 주요 이용 목적
    @PostMapping("/recommend")
    public ArrayList<RecommendCarDto> recommend(@RequestBody RecommendDto recommendDto) {
        Model model1 = null;       // 1순위
        Model model2 = null;       // 2순위

        Integer people = recommendDto.getAdultsCount() + recommendDto.getKidsCount();
        Integer minPrice = recommendDto.getMinPrice();
        if(minPrice == null)
            minPrice = 0;
        Integer maxPrice = recommendDto.getMaxPrice();
        if(maxPrice == null)
            maxPrice = Integer.MAX_VALUE;
        // 해당 인원수 이상, 예산 범위 내의 차량 목록 먼저 추리기
        ArrayList<Model> list = modelRepository.findByPeopleGreaterThanEqualAndPriceBetween(people, minPrice, maxPrice);

        // 만약 해당 범위 내 차량이 2개 이상일 경우 정렬하여 1,2순위 저장
        // 인원수 적은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순
        if(list.size() >= 2) {
            list = sortSmallPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                    recommendDto.getPurpose(), list);
            model1 = list.get(0);
            model2 = list.get(1);
        }

        // 만약 해당 범위 내 차량이 0개일 경우 -> 예산 범위 배제, 인원수만 고려하여 다시 뽑기
        else if(list.size() == 0) {
            list = modelRepository.findByPeopleGreaterThanEqual(people);
            // 해당 범위 내 차량이 2개 이상일 경우 -> 정렬
            if(list.size() >= 2) {
                list = sortSmallPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                        recommendDto.getPurpose(), list);
                model1 = list.get(0);
                model2 = list.get(1);
            }
            else {
                // 전체 모델 대상
                list = modelRepository.findAll();
                // 선택한 인원수가 최대 포용 인원보다 많아서 1개 또는 0개가 나온 경우
                // -> 인원수 많은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순
                if(people >= 9) {
                    list = sortBigPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                            recommendDto.getPurpose(), list);
                    model1 = list.get(0);
                    model2 = list.get(1);
                }
                // 선택한 인원수가 최소 포용 인원보다 적어서 1개 또는 0개가 나온 경우
                // -> 인원수 적은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순
                else {
                    list = sortSmallPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                            recommendDto.getPurpose(), list);
                    model1 = list.get(0);
                    model2 = list.get(1);
                }
            }
        }

        // 만약 해당 범위 내 차량이 1개일 경우 -> 1순위 확정, 2순위 추리기 -> 예산 범위 배제, 인원수만 고려하여 다시 뽑기
        else {
            model1 = list.get(0);
            list = modelRepository.findByPeopleGreaterThanEqual(people);
            // 해당 범위 내 차량이 1개일 경우, 이미 1순위로 선택한 모델이므로 다시 뽑아야함
            // 이 경우 인원수가 최대 포용 인원수보다 크거나 같은 경우이므로 전체 모델 대상으로
            // 인원수 많은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순 정렬
            if(list.size() <= 1) {
                list = modelRepository.findAll();
                list = sortBigPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                        recommendDto.getPurpose(), list);
                for(int i = 0; i < list.size(); i++) {
                    if(!list.get(i).getId().equals(model1.getId())) {
                        model2 = list.get(i);
                        break;
                    }
                }
            }
            // 해당 범위 내 차량이 2개 이상일 경우 -> 정렬
            else if(list.size() >= 2) {
                list = sortSmallPeople(recommendDto.getPriority1(), recommendDto.getPriority2(),
                        recommendDto.getPurpose(), list);
                for(int i = 0; i < list.size(); i++) {
                    if(!list.get(i).getId().equals(model1.getId())) {
                        model2 = list.get(i);
                        break;
                    }
                }
            }
        }

        String m1Pri = model1.bestPriority();
        int m1PriPercent = (int)model1.priorityPercent(m1Pri);
        String m2Pri = model2.bestPriority();
        int m2PriPercent = (int)model2.priorityPercent(m2Pri);
        String m1Pur = model1.bestPurpose();
        int m1PurPercent = (int)model1.purposePercent(m1Pur);
        String m2Pur = model2.bestPurpose();
        int m2PurPercent = (int)model2.purposePercent(m2Pur);

        RecommendCarDto car1 = new RecommendCarDto(model1.getId(), model1.getName(), model1.getDescription(),
                model1.getInformationURL(), model1.getPrice(), m1Pri, m1PriPercent, m1Pur, m1PurPercent);
        RecommendCarDto car2 = new RecommendCarDto(model2.getId(), model2.getName(), model2.getDescription(),
                model2.getInformationURL(), model2.getPrice(), m2Pri, m2PriPercent, m2Pur, m2PurPercent);
        ArrayList<RecommendCarDto> result = new ArrayList<>();
        result.add(car1);
        result.add(car2);
        return result;
    }

    // ArrayList<Model> 정렬 함수
    // 인원수 적은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순
    public ArrayList<Model> sortSmallPeople(String p1, String p2, String purpose, ArrayList<Model> list) {
        Collections.sort(list, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                // 1. 인원수 적은 순
                if (m1.getPeople() < m2.getPeople())
                    return -1;
                else if (m1.getPeople() > m2.getPeople())
                    return 1;
                else {
                    // 2. 우선 고려 사항 점수 높은 순
                    if (m1.twoPriorityScore(p1, p2) > m2.twoPriorityScore(p1, p2))
                        return -1;
                    else if (m1.twoPriorityScore(p1, p2) < m2.twoPriorityScore(p1, p2))
                        return 1;
                    else {
                        // 3. 주요 이용 목적 점수 높은 순
                        if (m1.purposePercent(purpose) > m2.purposePercent(purpose))
                            return -1;
                        else if (m1.purposePercent(purpose) < m2.purposePercent(purpose))
                            return 1;
                        else {
                            // 4. 저렴한 순
                            if (m1.getPrice() < m2.getPrice())
                                return -1;
                            else if (m1.getPrice() > m2.getPrice())
                                return 1;
                            else {
                                // 5. 이름 순
                                return m1.getName().compareTo(m2.getName());
                            }
                        }
                    }
                }
            }
        });
        return list;
    }

    // ArrayList<Model> 정렬 함수
    // 인원수 많은 순 - 우선 고려 사항 점수 높은 순 - 주요 이용 목적 점수 높은 순 - 저렴한 순 - 이름 순
    public ArrayList<Model> sortBigPeople(String p1, String p2, String purpose, ArrayList<Model> list) {
        Collections.sort(list, new Comparator<Model>() {
            @Override
            public int compare(Model m1, Model m2) {
                // 1. 인원수 많은 순
                if (m1.getPeople() > m2.getPeople())
                    return -1;
                else if (m1.getPeople() < m2.getPeople())
                    return 1;
                else {
                    // 2. 우선 고려 사항 점수 높은 순
                    if (m1.twoPriorityScore(p1, p2) > m2.twoPriorityScore(p1, p2))
                        return -1;
                    else if (m1.twoPriorityScore(p1, p2) < m2.twoPriorityScore(p1, p2))
                        return 1;
                    else {
                        // 3. 주요 이용 목적 점수 높은 순
                        if (m1.purposePercent(purpose) > m2.purposePercent(purpose))
                            return -1;
                        else if (m1.purposePercent(purpose) < m2.purposePercent(purpose))
                            return 1;
                        else {
                            // 4. 저렴한 순
                            if (m1.getPrice() < m2.getPrice())
                                return -1;
                            else if (m1.getPrice() > m2.getPrice())
                                return 1;
                            else {
                                // 5. 이름 순
                                return m1.getName().compareTo(m2.getName());
                            }
                        }
                    }
                }
            }
        });
        return list;
    }
}
