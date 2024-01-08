package warmingUp.antifragile.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warmingUp.antifragile.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByLoginIdAndPassword(String loginId, String password);
}
