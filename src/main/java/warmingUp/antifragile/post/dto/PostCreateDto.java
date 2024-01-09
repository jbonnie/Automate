package warmingUp.antifragile.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.car.domain.Car;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.post.domain.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {
    private Long id;
    private String title;
    private String contents;
    private Long mgp;
    private Long safe;
    private Long space;
    private Long design;
    private Long fun;
    private String purpose;

    public static Post toEntity(Member member, Car car, PostCreateDto postCreateDto){

        Post post = Post.builder()
                .writerId(member.getId())
                .carId(car.getId())
                .title(postCreateDto.getTitle())
                .contents(postCreateDto.getContents())
                .mgp(postCreateDto.getMgp())
                .safe(postCreateDto.getSafe())
                .space(postCreateDto.getSpace())
                .design(postCreateDto.getDesign())
                .fun(postCreateDto.getFun())
                .purpose(postCreateDto.getPurpose())
                .commentCount(0L)
                .build();
        return post;
    }
}
