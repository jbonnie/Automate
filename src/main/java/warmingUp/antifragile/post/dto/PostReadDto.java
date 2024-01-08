package warmingUp.antifragile.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.car.domain.Car;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.comment.dto.CommentSendDto;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReadDto {
    private String modelName;
    private String description;
    private String title;
    private String nickname;
    private LocalDateTime updatedAt;
    private Integer carAge;
    private Integer useYear;
    private Integer useMonth;
    private String purpose;
    private Long mgp;
    private Long safe;
    private Long space;
    private Long design;
    private Long fun;
    private String contents;
    private Long commentCount;
    private List<CommentSendDto> commentList;

    public static PostReadDto fromEntity(Post post, List<CommentSendDto> commentSendDtos, Model model, Member member, Car car){
        PostReadDto postReadDto = PostReadDto.builder()
                .modelName(model.getName())
                .description(model.getDescription())
                .title(post.getTitle())
                .nickname(member.getNickname())
                .updatedAt(post.getUpdatedAt())
                .carAge(car.getCarAge())
                .useYear(LocalDateTime.now().getYear()- car.getBuyYear())
                .useMonth(LocalDateTime.now().getMonthValue()- car.getBuyMonth())
                .purpose(post.getPurpose())
                .mgp(post.getMgp())
                .safe(post.getSafe())
                .space(post.getSpace())
                .design(post.getDesign())
                .fun(post.getFun())
                .contents(post.getContents())
                .commentCount(post.getCommentCount())
                .commentList(commentSendDtos)
                .build();
        return postReadDto;
    }
}
