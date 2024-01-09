package warmingUp.antifragile.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import warmingUp.antifragile.car.domain.Car;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.car.repository.CarRepository;
import warmingUp.antifragile.car.repository.ModelRepository;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.comment.dto.CommentSendDto;
import warmingUp.antifragile.comment.repository.CommentRepository;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.member.repository.MemberRepository;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.*;
import warmingUp.antifragile.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    CommentRepository commentRepository;

    public ReturnManyDto<PostThumbnailDto> getThumbnails(){
        //모든 Post 인스턴스를 리스트에 저장 후 순회
        List<Post> posts = postRepository.findAll();
        // Post 객체를 PostThumnailDto로 변환 후 리스트 반환
        ArrayList<PostThumbnailDto> list = postList2ThumnailList(posts);
        // 리스트 최신순 정렬
        list.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<>(list,"조회성공" );
    }
    
    // Post 객체로 이루어진 리스트를 PostThumnail 객체로 바꾸어 리스트 반환
    public ArrayList<PostThumbnailDto> postList2ThumnailList(List<Post> posts) {
        ArrayList<PostThumbnailDto> result = new ArrayList<>();
        //순회하며 PostThumbnailDto로 변환
        for(Post post : posts){
            Member member = memberRepository.findById(post.getWriterId()).orElse(null);
            if(member == null)
                continue;
            Car car = carRepository.findById(post.getCarId()).orElse(null);
            if(car == null)
                continue;
            Model model = modelRepository.findById(car.getModelId()).orElse(null);
            if(model == null)
                continue;
            PostThumbnailDto postThumbnailDto = PostThumbnailDto.fromEntities(post, model, member, car);
            result.add(postThumbnailDto);
        }
        return result;
    }

    public ReturnOneDto<PostReadDto> getPostById(Long postId){
        //postId값 null이면 중단
        if(postId == null)
            return new ReturnOneDto<>(null,"null값을 postId로 받았습니다");

        //Post값 조회 실패
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<>(null,"Post를 찾지 못했음");

        //차 조회 실패
        Car car = carRepository.findById(post.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<>(null,"차를 찾지 못했음");

        //차 모델 조회 실패
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<>(null,"모델을 찾지 못했음");

        //맴버 조회 실패
        Member member = memberRepository.findById(post.getWriterId()).orElse(null);
        if(member == null)
            return new ReturnOneDto<>(null,"맴버를 찾지 못했음");

        // 댓글 엔티티에서 postId로 필터링, 필터링 후 for문으로 Dto로 변환
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentSendDto> commentSendDtos = new ArrayList<>();
        for(Comment comment: comments){
            CommentSendDto commentSendDto = CommentSendDto.fromEntities(post, comment, member);
            commentSendDtos.add(commentSendDto);
        }

        // 리뷰 Dto에 각종 정보와 댓글List를 주입 시킨 후 리턴 -> 현재는 댓글 구현하지 않으므로 null 주입
        PostReadDto postReadDto = PostReadDto.fromEntity(post, commentSendDtos, model, member, car);
        return new ReturnOneDto<>(postReadDto, "리뷰 불러오기 성공");

    }

    public ReturnOneDto<PostReadDto> createPost(Long memberId, PostCreateDto postCreateDto){
        //맴버조회 실패
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            return new ReturnOneDto<>(null, "등록된 회원이 아닙니다");
        //차 조회 실패
        Car car = carRepository.findById(member.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<>(null, "차를 등록하지 않았습니다");
        // DB에 저장
        Post post = PostCreateDto.toEntity(member, car, postCreateDto);
        Post saved = postRepository.save(post);
        // 게시물에 해당하는 차량 모델에 리뷰글 개수 + 1
        // 게시물에 해당하는 차량 모델에 고려사항 누적 점수, 목적 누적 개수 반영
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<>(null,"저장은 했으나 해당 모델을 찾지 못했음");
        model.setReviewCount(model.getReviewCount() + 1);
        model.setMpgSum(model.getMpgSum() + ((postCreateDto.getMgp()==null)?5L:postCreateDto.getMgp()));
        model.setSafeSum(model.getSafeSum() + ((postCreateDto.getSafe()==null)?5L:postCreateDto.getSafe()));
        model.setSpaceSum(model.getSpaceSum() + ((postCreateDto.getSpace()==null)?5L:postCreateDto.getSpace()));
        model.setDesignSum(model.getDesignSum() + ((postCreateDto.getDesign()==null)?5L:postCreateDto.getDesign()));
        model.setFunSum(model.getFunSum() + ((postCreateDto.getFun()==null)?5L:postCreateDto.getFun()));

        if(postCreateDto.getPurpose() != null) {
            switch (postCreateDto.getPurpose()) {
                case "출퇴근용":
                    model.setWorkCount(model.getWorkCount() + 1);
                    break;
                case "장거리 운전":
                    model.setLongCount(model.getLongCount() + 1);
                    break;
                case "드라이브":
                    model.setDriveCount(model.getDriveCount() + 1);
                    break;
                case "주말여행":
                    model.setTravelCount(model.getTravelCount() + 1);
                    break;
                case "자녀와 함께":
                    model.setKidsCount(model.getKidsCount() + 1);
                    break;
            }
        }
        Model modelSaved = modelRepository.save(model);

        // 댓글은 없을테니 commentSendDtos는 null값으로 대체
        // PostReadDto를 생성 후 컨트롤러에 전달
        PostReadDto postReadDto = PostReadDto.fromEntity(saved, null, modelSaved, member, car);
        return new ReturnOneDto<>(postReadDto, "리뷰 등록성공");
    }

    public ReturnOneDto<PostReadDto> deletePost(Long memberId,Long postId){
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<>(null,"리뷰를 찾을 수 없습니다");

        if(!post.getWriterId().equals(memberId))
            return new ReturnOneDto<>(null, "작성자 본인만 삭제 할 수 있습니다");
        // 해당 게시물의 댓글 모두 삭제
        commentRepository.deleteAllByPostId(postId);
        // 해당 게시물의 차량 가져오기
        Car car = carRepository.findById(post.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<>(null, "게시물에 차량이 없습니다.");
        // 해당 게시물의 모델 가져오기
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<>(null, "게시물에 해당하는 차량 모델이 없습니다.");
        // 해당 게시물이 매긴 점수 모델 누적합에서 빼주기
        model.setMpgSum(model.getMpgSum() - ((post.getMgp()==null)?5L:post.getMgp()));
        model.setSafeSum(model.getSafeSum() - ((post.getSafe()==null)?5L:post.getSafe()));
        model.setSpaceSum(model.getSpaceSum() - ((post.getSpace()==null)?5L:post.getSpace()));
        model.setDesignSum(model.getDesignSum() - ((post.getDesign()==null)?5L:post.getDesign()));
        model.setFunSum(model.getFunSum() - ((post.getFun()==null)?5L:post.getFun()));
        if(post.getPurpose() != null) {
            switch (post.getPurpose()) {
                case "출퇴근용":
                    model.setWorkCount(model.getWorkCount() - 1);
                    break;
                case "장거리 운전":
                    model.setLongCount(model.getLongCount() - 1);
                    break;
                case "드라이브":
                    model.setDriveCount(model.getDriveCount() - 1);
                    break;
                case "주말여행":
                    model.setTravelCount(model.getTravelCount() - 1);
                    break;
                case "자녀와 함께":
                    model.setKidsCount(model.getKidsCount() - 1);
                    break;
            }
        }
        // 해당 모델의 리뷰 게시물 개수 - 1
        model.setReviewCount(model.getReviewCount() - 1);
        modelRepository.save(model);
        // 해당 게시물 삭제
        postRepository.delete(post);

        return new ReturnOneDto<>(null, "리뷰 삭제 성공");
    }

    public ReturnOneDto<PostReadDto> modifyPost(Long memberId, Long postId, PostCreateDto postCreateDto){

        // 해당하는 기존의 Post 객체 불러오기
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<>(null, "리뷰를 찾을 수 없습니다.");
        // 글 작성자의 접근인지 확인
        if(!memberId.equals(post.getWriterId()))
            return new ReturnOneDto<>(null, "작성자만 수정 가능해요.");
        // 유저 객체 가져오기
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            return new ReturnOneDto<>(null, "당신은 유령인가요?");

        // 수정 전 모델에 해당하는 평가들 초기화
        Car car = carRepository.findById(post.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<>(null, "리뷰에 해당하는 차량이 없었네요.");
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<>(null, "리뷰에 해당하는 모델이 없어요.");

        model.setMpgSum(model.getMpgSum() - ((post.getMgp()==null)?5L:post.getMgp()));
        model.setSafeSum(model.getSafeSum() - ((post.getSafe()==null)?5L:post.getSafe()));
        model.setSpaceSum(model.getSpaceSum() - ((post.getSpace()==null)?5L:post.getSpace()));
        model.setDesignSum(model.getDesignSum() - ((post.getDesign()==null)?5L:post.getDesign()));
        model.setFunSum(model.getFunSum() - ((post.getFun()==null)?5L:post.getFun()));
        if(post.getPurpose() != null) {
            switch (post.getPurpose()) {
                case "출퇴근용":
                    model.setWorkCount(model.getWorkCount() - 1);
                    break;
                case "장거리 운전":
                    model.setLongCount(model.getLongCount() - 1);
                    break;
                case "드라이브":
                    model.setDriveCount(model.getDriveCount() - 1);
                    break;
                case "주말여행":
                    model.setTravelCount(model.getTravelCount() - 1);
                    break;
                case "자녀와 함께":
                    model.setKidsCount(model.getKidsCount() - 1);
                    break;
            }
        }

        // 모델 점수 수정사항 반영
        model.setMpgSum(model.getMpgSum() + ((postCreateDto.getMgp()==null)?5L:postCreateDto.getMgp()));
        model.setSafeSum(model.getSafeSum() + ((postCreateDto.getSafe()==null)?5L:postCreateDto.getSafe()));
        model.setSpaceSum(model.getSpaceSum() + ((postCreateDto.getSpace()==null)?5L:postCreateDto.getSpace()));
        model.setDesignSum(model.getDesignSum() + ((postCreateDto.getDesign()==null)?5L:postCreateDto.getDesign()));
        model.setFunSum(model.getFunSum() + ((postCreateDto.getFun()==null)?5L:postCreateDto.getFun()));
        if(post.getPurpose() != null) {
            switch (post.getPurpose()) {
                case "출퇴근용":
                    model.setWorkCount(model.getWorkCount() + 1);
                    break;
                case "장거리 운전":
                    model.setLongCount(model.getLongCount() + 1);
                    break;
                case "드라이브":
                    model.setDriveCount(model.getDriveCount() + 1);
                    break;
                case "주말여행":
                    model.setTravelCount(model.getTravelCount() + 1);
                    break;
                case "자녀와 함께":
                    model.setKidsCount(model.getKidsCount() + 1);
                    break;
            }
        }
        Model modelSaved = modelRepository.save(model);

        // post 객체 수정사항 반영
        Post modifiedPost = PostCreateDto.toEntity(member, car, postCreateDto);
        modifiedPost.setId(postId);
        Post postSaved = postRepository.save(modifiedPost);

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentSendDto> commentSendDtos = new ArrayList<>();
        for(Comment comment: comments){
            Member commentMember = memberRepository.findById(comment.getWriterId()).orElse(null);
            if(commentMember == null)
                continue;
            CommentSendDto commentSendDto = CommentSendDto.fromEntities(postSaved, comment, commentMember);
            commentSendDtos.add(commentSendDto);
        }
        // 수정된 postReadDto 보내기
        PostReadDto postReadDto = PostReadDto.fromEntity(postSaved, commentSendDtos, modelSaved, member, car);
        return new ReturnOneDto<>(postReadDto, "리뷰 수정 성공");
    }


    public ReturnManyDto<CommentSendDto> getCommentsByPostId(Long postId){
        // 댓글 엔티티에서 postId로 필터링, 필터링 후 for문으로 Dto로 변환
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        ArrayList<CommentSendDto> commentSendDtos = new ArrayList<>();

        for(Comment comment: comments){

            Post post = postRepository.findById(comment.getPostId()).orElse(null);
            if(post == null)// post 조회실패시 해당 댓글은 추가하지 않는다
                continue;

            Member member = memberRepository.findById(comment.getWriterId()).orElse(null);
            if(member == null)// member 조회실패시 해당 댓글은 추가하지 않는다
                continue;

            CommentSendDto commentSendDto = CommentSendDto.fromEntities(post, comment, member);
            commentSendDtos.add(commentSendDto);
        }

        //최신순으로 정렬 후 컨트롤러에 전달
        commentSendDtos.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<>(commentSendDtos,"조회성공" );
    }

    // 필터링 후 Post 객체로 목록 가져오기
    public List<Post> filterPost(String model, String type, String purpose,
                                 Integer minPrice, Integer maxPrice,
                                 String keyword) {
        List<Post> list;

        // 선택한 주요 이용 목적 리스트 만들기
        ArrayList<String> purposes = new ArrayList<>();
        if(purpose != null) {
            StringTokenizer stk = new StringTokenizer(purpose, ",");
            while(stk.hasMoreTokens()) {
                purposes.add(stk.nextToken());
            }
        }
        // 선택한 차종 리스트 만들기
        ArrayList<String> types = new ArrayList<>();
        if(type != null) {
            StringTokenizer stk = new StringTokenizer(type, ",");
            while(stk.hasMoreTokens()) {
                types.add(stk.nextToken());
            }
        }

        // 1. purpose와 검색어가 모두 선택되었다면
        if(!purposes.isEmpty() && keyword != null) {
            // 선택된 주요 목적, 검색어가 모두 포함된 게시물 리스트 추출
            list = postRepository.findByPurposeInAndContentsContaining(purposes, keyword);
        }

        // 2. purpose만 선택되었을 경우
        else if(!purposes.isEmpty()) {
            // 선택된 주요 목적이 포함된 게시물 리스트 추출
            list = postRepository.findByPurposeIn(purposes);
        }
        // 3. 검색어만 선택되었을 경우
        else if(keyword != null) {
            // 검색어가 내용에 포함된 게시물 리스트 추출
            list = postRepository.findByContentsContaining(keyword);
        }
        // purpose와 검색어가 모두 선택되지 않았을 경우
        else {
            // 모든 게시물 리스트 추출
            list = postRepository.findAll();
        }

        if(minPrice == null)
            minPrice = 0;
        if(maxPrice == null)
            maxPrice = Integer.MAX_VALUE;

        System.out.println("size: " + list.size());

        // Post list 순회하며 선택한 모델, 예산 범위에 해당하는 것들로만 추리기
        for(Post post : list) {
            Car car = carRepository.findById(post.getCarId()).orElse(null);
            if(car == null) {
                list.remove(post);
                continue;
            }
            Model m = modelRepository.findById(car.getModelId()).orElse(null);
            if(m == null) {
                list.remove(post);
                continue;
            }
            String modelName = m.getName();     // 해당 게시물의 차량 모델명
            Integer modelPrice = m.getPrice();      // 해당 게시물의 차량 가격

            // 선택한 모델명에 해당하지 않을 시 리스트에서 삭제
            if(model != null && !modelName.equals(model)) {
                list.remove(post);
                System.out.println("not my model");
                continue;
            }

            // 선택한 예산 범위 내에 해당하지 않을 시 리스트에서 삭제
            if(!(minPrice <= modelPrice && modelPrice <= maxPrice)) {
                list.remove(post);
                System.out.println("not my price");
                System.out.println("size: " + list.size());
            }
        }
        System.out.println("good4");
        return list;
    }

    // 선택된 정렬 기준으로 정렬하기 (최신순 / 오래된 순 / 댓글 많은 순)
    // 디폴트: 최신순
    public ArrayList<PostThumbnailDto> sortPostThumbnailList(ArrayList<PostThumbnailDto> list, String sort) {
        if(sort == null || sort.equals("최신 순")) {
            list.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        }
        else if(sort.equals("오래된 순")) {
            list.sort((o1, o2) -> o1.getUpdatedAt().compareTo(o2.getUpdatedAt()));
        }
        else {      // 댓글 많은 순
            list.sort((o1, o2) -> o2.getCommentCount().compareTo(o1.getCommentCount()));
        }
        return list;
    }
}
