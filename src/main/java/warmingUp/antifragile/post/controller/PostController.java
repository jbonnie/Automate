package warmingUp.antifragile.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.post.dto.*;
import warmingUp.antifragile.post.service.PostService;


@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/reviews")
    public ReturnManyDto<PostThumbnailDto> getReviews(){
        return postService.getThumbnails();

    }

    @GetMapping("/reviews/{postId}")
    public ReturnOneDto<PostReadDto> getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }

    @PostMapping("/reviews")
    public ReturnOneDto<PostReadDto> createPost(@RequestBody PostCreateDto postCreateDto, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long memberId = (Long)session.getAttribute("memberID");
        //로그인이 안되어 있을 때
        if(memberId == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");
        //맴버 조회실패, 차가 없는 경우, 성공했을 경우
        return postService.createPost(memberId, postCreateDto);
    }

    @DeleteMapping("/reviews/{postId}")
    public ReturnOneDto<PostReadDto> deletePost(@PathVariable Long postId, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long memberId = (Long)session.getAttribute("memberID");

        //로그인 안되어 있는 경우
        if(memberId == null)
            return new ReturnOneDto<>(null,"로그인을 하세요");
        // 그 밖의 경우
        return postService.deletePost(memberId,postId);

    }

    @PatchMapping("/reviews/{postId}")
    public ReturnOneDto<PostReadDto> modifyPost(@PathVariable Long postId,@RequestBody PostCreateDto postCreateDto, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long memberId = (Long)session.getAttribute("memberID");

        //로그인 안되어 있는 경우
        if(memberId == null)
            return new ReturnOneDto<>(null,"로그인을 하세요");

        return postService.modifyPost(memberId,postId, postCreateDto);

    }
}
