package warmingUp.antifragile.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.car.domain.Car;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.post.domain.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostThumbnailDto {
    private Long id;
    private String title;
    private String modelName;
    private Integer carAge;
    private String nickname;
    private LocalDateTime updatedAt;
    private Long commentCount;

    public static PostThumbnailDto fromEntities(Post post, Model model, Member member, Car car) {
        return PostThumbnailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .modelName(model.getName())
                .carAge(car.getCarAge())
                .nickname(member.getNickname())
                .updatedAt(post.getUpdatedAt())
                .commentCount(post.getCommentCount())
                .build();
    }

}
