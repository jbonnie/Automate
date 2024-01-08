package warmingUp.antifragile.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByWriterId(Long id);
}
