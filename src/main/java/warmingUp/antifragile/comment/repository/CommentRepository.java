package warmingUp.antifragile.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.comment.domain.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByWriterId(Long id);

    List<Comment> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);

    List<Comment> findAllByWriterId(Long memberId);
}
