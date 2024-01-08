package warmingUp.antifragile.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.comment.dto.CommentSendDto;
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
        //로그인이 안되어 있을 때
        HttpSession session = request.getSession(false);
        if(session == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");

        Long memberId = (Long)session.getAttribute("memberID");
        if(memberId == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");
        //맴버 조회실패, 차가 없는 경우, 성공했을 경우
        return postService.createPost(memberId, postCreateDto);
    }

    @DeleteMapping("/reviews/{postId}")
    public ReturnOneDto<PostReadDto> deletePost(@PathVariable Long postId, HttpServletRequest request){
        //로그인이 안되어 있을 때
        HttpSession session = request.getSession(false);
        if(session == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");

        Long memberId = (Long)session.getAttribute("memberID");
        if(memberId == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");
        // 그 밖의 경우
        return postService.deletePost(memberId,postId);

    }

    @PatchMapping("/reviews/{postId}")
    public ReturnOneDto<PostReadDto> modifyPost(@PathVariable Long postId,@RequestBody PostCreateDto postCreateDto, HttpServletRequest request){
        //로그인이 안되어 있을 때
        HttpSession session = request.getSession(false);
        if(session == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");

        Long memberId = (Long)session.getAttribute("memberID");
        if(memberId == null)
            return new ReturnOneDto<>(null, "로그인을 하세요");

        return postService.modifyPost(memberId,postId, postCreateDto);
    }

    @GetMapping("/reviews/{postId}/commments")
    public ReturnManyDto<CommentSendDto> getCommentsByPostId(@PathVariable Long postId){
        if(postId==null)
            return new ReturnManyDto<>(null,"postId를 null값을 받았습니다");
        return postService.getCommentsByPostId(postId);
    }

//    @GetMapping("/reviews?model=&type=&purpose&minPrice=&maxPrice=&keyword=&sort=")
//    public ReturnManyDto<PostThumbnailDto> getRefinedReviews(@PathVariable String )
}
