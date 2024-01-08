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
        List<Post> posts = postRepository.findAll();
        ArrayList<PostThumbnailDto> postThumbnailDtos = new ArrayList<>();

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
        postThumbnailDtos.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<PostThumbnailDto>(postThumbnailDtos,"조회성공" );
    }

    public ReturnOneDto<PostReadDto> getPostById(Long postId){
        if(postId == null)
            return new ReturnOneDto<PostReadDto>(null,"null값을 postId로 받았습니다");

        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<PostReadDto>(null,"Post를 찾지 못했음");

        Car car = carRepository.findById(post.getCarId()).orElse(null);
        if(car == null)
            return new ReturnOneDto<PostReadDto>(null,"차를 찾지 못했음");

        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<PostReadDto>(null,"모델을 찾지 못했음");

        Member member = memberRepository.findById(post.getWriterId()).orElse(null);
        if(member == null)
            return new ReturnOneDto<PostReadDto>(null,"맴버를 찾지 못했음");

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentSendDto> commentSendDtos = new ArrayList<>();
        for(Comment comment: comments){
            CommentSendDto commentSendDto = CommentSendDto.fromEntities(post, comment, member);
            commentSendDtos.add(commentSendDto);
        }

        PostReadDto postReadDto = PostReadDto.fromEntity(post, commentSendDtos, model, member, car);

        return new ReturnOneDto<PostReadDto>(postReadDto, "리뷰 불러오기 성공");

    }

    public ReturnOneDto<PostReadDto> createPost(Long memberId, PostCreateDto postCreateDto){

        Car car = carRepository.findById(memberId).orElse(null);
        if(car == null)
            return new ReturnOneDto<PostReadDto>(null, "차를 등록하지 않았습니다");

        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            return new ReturnOneDto<PostReadDto>(null, "등록된 회원이 아닙니다");
        Post post = PostCreateDto.toEntity(member, car, postCreateDto);
        Post saved = postRepository.save(post);


        Model model = modelRepository.findById(car.getModelId()).orElse(null);
        if(model == null)
            return new ReturnOneDto<PostReadDto>(null,"모델을 찾지 못했음");

        List<CommentSendDto> commentSendDtos = commentRepository.findALLCommentByPostId(saved.getId());

        PostReadDto postReadDto = PostReadDto.fromEntity(saved, commentSendDtos, model, member, car);
        return new ReturnOneDto<PostReadDto>(postReadDto, "리뷰 등록성공");








    }


}
