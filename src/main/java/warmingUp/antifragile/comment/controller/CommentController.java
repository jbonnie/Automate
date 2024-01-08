package warmingUp.antifragile.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.annotations.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.comment.dto.CommentReceiveDto;
import warmingUp.antifragile.comment.dto.CommentSendDto;
import warmingUp.antifragile.comment.repository.CommentRepository;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.member.repository.MemberRepository;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.repository.PostRepository;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    // 댓글 생성
    @PostMapping("/reviews/{postId}/comments")
    public CommentSendDto createComment(@RequestBody CommentReceiveDto commentReceiveDto,
                                        @PathVariable Long postId,
                                        HttpServletRequest request) {
        // 현재 유저 객체 가져오기
        HttpSession session = request.getSession(false);
        if(session == null)
            return null;
        Long id = (Long)session.getAttribute("memberID");
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null)
            return null;

        // 댓글 객체 생성 및 저장
        Comment comment = new Comment(null, postId, id, commentReceiveDto.getContents());
        Comment saved = commentRepository.save(comment);

        // 댓글이 달리는 게시물 가져오기 -> 댓글 개수 + 1
        Post post = postRepository.findById(postId).orElse(null);
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        CommentSendDto result = new CommentSendDto(postId, null, saved.getContents(), member.getNickname(),saved.getUpdatedAt());
        if(post != null)
            result.setPostTitle(post.getTitle());
        return result;
    }

    // 댓글 수정
    @PatchMapping("/reviews/{postId}/comments/{commentId}")
    public CommentSendDto updateComment(@PathVariable Long postId,
                                  @PathVariable Long commentId,
                                  @RequestBody CommentReceiveDto commentReceiveDto,
                                  HttpServletRequest request) {
        // 기존의 댓글 객체 가져오기
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            return null;

        // 현재 유저가 댓글 작성자인지 확인
        HttpSession session = request.getSession(false);
        if(session == null)
            return null;
        Long id = (Long)session.getAttribute("memberID");
        // 작성자가 아닐 경우
        if(!comment.getWriterId().equals(id))
            return null;
        // 성공적인 댓글 수정
        comment.setContents(commentReceiveDto.getContents());
        Comment updateComment = commentRepository.save(comment);
        // 댓글이 달린 게시물 객체 가져오기
        Post post = postRepository.findById(postId).orElse(null);
        // 현재 유저 객체 가져오기
        Member member = memberRepository.findById(id).orElse(null);

        CommentSendDto result = new CommentSendDto(postId, null,updateComment.getContents(), null, updateComment.getUpdatedAt());
        if(post != null)
            result.setPostTitle(post.getTitle());
        if(member != null)
            result.setWriterNickname(member.getNickname());
        return result;
    }

    // 댓글 삭제
    @DeleteMapping("/reviews/{postId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                HttpServletRequest request) {
        // 현재 유저 객체 가져오기
        HttpSession session = request.getSession(false);
        if(session == null)
            return "로그인해주세요.";
        Long id = (Long)session.getAttribute("memberID");

        // 삭제 대상인 댓글 객체 가져오기
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment == null)
            return "존재하지 않는 댓글입니다.";
        // 댓글 작성자가 아닐 경우
        if(!comment.getWriterId().equals(id))
            return "작성자만 삭제 가능해요.";
        // 댓글 작성자일 경우
        commentRepository.delete(comment);
        // 해당 게시물의 댓글 개수 - 1
        Post post = postRepository.findById(postId).orElse(null);
        if(post != null) {
            post.setCommentCount(post.getCommentCount() - 1);
            postRepository.save(post);
        }
        return "삭제되었어요.";
    }
}
