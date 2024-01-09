package warmingUp.antifragile.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.PostThumbnailDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteAllByWriterId(Long id);


    Optional<Post> findById(Long postId);
    List<Post> findAllByWriterId(Long memberId);
    List<Post> findByPurposeInAndContentsContaining(ArrayList<String> purposes, String keyword);
    List<Post> findByPurposeIn(ArrayList<String> purposes);
    List<Post> findByContentsContaining(String keyword);
}
