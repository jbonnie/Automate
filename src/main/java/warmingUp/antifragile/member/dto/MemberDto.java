package warmingUp.antifragile.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String carNum;
    private String modelName;
    private Integer carAge;
    private Integer buyYear;
    private Integer buyMonth;
}
