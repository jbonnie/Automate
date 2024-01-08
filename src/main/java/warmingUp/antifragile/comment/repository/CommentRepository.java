package warmingUp.antifragile.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.comment.dto.CommentSendDto;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByWriterId(Long id);

    List<Comment> findAllByPostId(Long postId);

    List<CommentSendDto> findALLCommentByPostId(Long id);
}
