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
import java.util.Collections;
import java.util.List;

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
        ArrayList<PostThumbnailDto> postThumbnailDtos = new ArrayList<>();

        //순회하며 PostThumbnailDto로 변환
        for(Post post : posts){
            Member member = memberRepository.findById(post.getWriterId()).orElse(null);
            if(member == null)
                continue;
            Car car = carRepository.findById(member.getCarId()).orElse(null);
            if(car == null)
                continue;
            Model model = modelRepository.findById(car.getModelId()).orElse(null);
            if(model == null)
                continue;
            PostThumbnailDto postThumbnailDto = PostThumbnailDto.fromEntities(post, model, member, car);
            postThumbnailDtos.add(postThumbnailDto);
        }

        //최신순으로 정렬 후 컨트롤러에 전달
        postThumbnailDtos.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<PostThumbnailDto>(postThumbnailDtos,"조회성공" );
    }

    public ReturnOneDto<PostReadDto> getPostById(Long postId){
        //postId값 null이면 중단
        if(postId == null)
            return new ReturnOneDto<PostReadDto>(null,"null값을 postId로 받았습니다");
₩
        //Post값 조회 실패
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<PostReadDto>(null,"Post를 찾지 못했음");

        //차 조회 실패
        Car car = carRepository.findById(post.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<PostReadDto>(null,"차를 찾지 못했음");

        //차 모델 조회 실패
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<PostReadDto>(null,"모델을 찾지 못했음");

        //맴버 조회 실패
        Member member = memberRepository.findById(post.getWriterId()).orElse(null);
        if(member == null)
            return new ReturnOneDto<PostReadDto>(null,"맴버를 찾지 못했음");

        // 댓글 엔티티에서 postId로 필터링, 필터링 후 for문으로 Dto로 변환
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentSendDto> commentSendDtos = new ArrayList<>();
        for(Comment comment: comments){
            CommentSendDto commentSendDto = CommentSendDto.fromEntities(post, comment, member);
            commentSendDtos.add(commentSendDto);
        }

        // 리뷰 Dto에 각종 정보와 댓글List를 주입 시킨 후 리턴
        PostReadDto postReadDto = PostReadDto.fromEntity(post, commentSendDtos, model, member, car);

        return new ReturnOneDto<PostReadDto>(postReadDto, "리뷰 불러오기 성공");

    }

    public ReturnOneDto<PostReadDto> createPost(Long memberId, PostCreateDto postCreateDto){
        //차 조회 실패
        Car car = carRepository.findById(memberId).orElse(null);
        if(car == null)
            return new ReturnOneDto<PostReadDto>(null, "차를 등록하지 않았습니다");
        //맴버조회 실패
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            return new ReturnOneDto<PostReadDto>(null, "등록된 회원이 아닙니다");

        // DB에 저장
        Post post = PostCreateDto.toEntity(member, car, postCreateDto);
        Post saved = postRepository.save(post);

        //Post에 저장할때는 필요 없지만 PostReadDto에는 Model 엔티티 조회가 필요
        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<PostReadDto>(null,"모델을 찾지 못했음");


        // 댓글은 없을테니 commentSendDtos는 null값으로 대체
        // PostReadDto를 생성 후 컨트롤러에 전달
        PostReadDto postReadDto = PostReadDto.fromEntity(saved, null, model, member, car);
        return new ReturnOneDto<PostReadDto>(postReadDto, "리뷰 등록성공");


    }

    public ReturnOneDto<PostReadDto> deletePost(Long memberId,Long postId){

    }


}
