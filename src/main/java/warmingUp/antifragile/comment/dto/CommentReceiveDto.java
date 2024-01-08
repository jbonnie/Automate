package warmingUp.antifragile.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReceiveDto {

    private Long id;
    private String contents;
}
