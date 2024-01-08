package warmingUp.antifragile.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostThumbnailDto {
    private Long id;
    private String title;
    private String modelName;
    private Integer carAge;
    private String nickname;
    private LocalDateTime updatedAt;
    private Long commentCount;
}
