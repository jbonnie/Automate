package warmingUp.antifragile.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.PostThumbnailDto;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteAllByWriterId(Long id);

//    @Query("SELECT new warmingUp.antifragile.post.dto.PostThumbnailDto(p.id, p.title, m.modelName, m.carAge, mem.nickname, p.updatedAt, p.commentCount) " +
//            "FROM Post p, Model m, Member mem, Car c " +
//            "WHERE p.writerId = mem.id AND p.carId = c.id AND c.modelId = m.id " +
//            "ORDER BY p.updatedAt DESC")
//    List<PostThumbnailDto> findThumbnails();


}
