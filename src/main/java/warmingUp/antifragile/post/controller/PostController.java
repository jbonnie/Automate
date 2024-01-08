package warmingUp.antifragile.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.PostThumbnailDto;
import warmingUp.antifragile.post.dto.ReturnManyDto;
import warmingUp.antifragile.post.service.PostService;

import java.util.ArrayList;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

//    @GetMapping("/reviews")
//    public ReturnManyDto<PostThumbnailDto> getReviews(){
////        return postService.getThumbnails();
////
//    }

//    @GetMapping("/reviews/{postId}")
//    public ReturnOneDto<PostReadDto> getPostById(@PathVariable Long postId){
//        PostReadDto postDto = postService.getPostDtoById(postId);
//        if(postDto == null)
//            return new ReturnOneDto<PostReadDto>(null, "게시물이 존재하지 않습니다");
//        return new ReturnOneDto<PostReadDto>(postDto, "게시물 반환 성공");
//    }
//
//    @PostMapping("/reviews")
//    public ReturnOneDto<Post> createPost(@RequestBody PostWriteDto postWriteDto, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        Long memberId = (Long)session.getAttribute("memberID");
//
//        //로그인이 안되어 있을 때
//        if(memberId == null)
//            return new ReturnOneDto<Post>(null, "로그인을 하세요");
//        //맴버 조회실패, 차가 없는 경우, 성공했을 경우
//        return postService.createPost(memberId, postWriteDto);
//    }
//
//    @DeleteMapping("/reviews/{postId}")
//    public ReturnOneDto<Post> deletePost(@PathVariable Long postId, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        Long memberId = (Long)session.getAttribute("memberID");
//
//        //로그인 안되어 있는 경우
//        if(memberId == null)
//            return new ReturnOneDto<Post>(null,"로그인을 하세요");
//
//        return postService.deletePost(memberId,postId);
//
//    }
//
//    @PatchMapping("/reviews/{postId}")
//    public ReturnOneDto<Post> modifyPost(@PathVariable Long postId,@RequestBody PostWriteDto postWriteDto, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        Long memberId = (Long)session.getAttribute("memberID");
//
//        //로그인 안되어 있는 경우
//        if(memberId == null)
//            return new ReturnOneDto<Post>(null,"로그인을 하세요");
//
//        return postService.modifyPost(memberId,postId, postWriteDto);
//
//    }
}
