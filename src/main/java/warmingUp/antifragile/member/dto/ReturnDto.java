package warmingUp.antifragile.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.member.domain.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDto {

    private Member member;
    private String message;
}
