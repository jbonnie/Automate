package warmingUp.antifragile.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSendDto {

    private Long postId;
    private String postTitle;
    private String commentContents;
    private String writerNickname;
    private LocalDateTime updatedAt;
}
