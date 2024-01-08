package warmingUp.antifragile.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    @Column
    private Long carId;

    public Member(Long id, String loginId, String password, String nickname) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }
}
