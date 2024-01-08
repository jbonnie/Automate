package warmingUp.antifragile.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import warmingUp.antifragile.member.domain.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    private Long id;
    private String loginId;
    private String password;
    private String nickname;

    public Member toEntity() {
        return new Member(id, loginId, password, nickname);
    }
}
