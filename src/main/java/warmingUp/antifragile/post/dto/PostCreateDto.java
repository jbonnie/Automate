package warmingUp.antifragile.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long purpose;
}
