package warmingUp.antifragile.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.post.domain.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentSendDto {

    private Long postId;
    private String postTitle;
    private String commentContents;
    private String writerNickname;
    private LocalDateTime updatedAt;

    public static CommentSendDto fromEntities(Post post, Comment comment, Member member) {
        CommentSendDto commentSendDto = CommentSendDto.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .commentContents(comment.getContents())
                .writerNickname(member.getNickname())
                .updatedAt(comment.getUpdatedAt())
                .build();
        return commentSendDto;
    }
}
