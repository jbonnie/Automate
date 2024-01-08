package warmingUp.antifragile.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.car.dto.CarDto;
import warmingUp.antifragile.member.dto.LoginDto;
import warmingUp.antifragile.member.dto.MemberDto;
import warmingUp.antifragile.member.dto.ReturnDto;
import warmingUp.antifragile.member.dto.SignupDto;
import warmingUp.antifragile.member.service.MemberService;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입 요청 처리
    // 중복된 아이디 : member - null / message - "이미 사용 중인 아이디입니다. 다시 시도해주세요."
    // 중복된 닉네임 : member - null / message - "이미 사용 중인 닉네임입니다. 다시 시도해주세요."
    // 회원가입 성공 : member - 생성된 엔티티 / message - 유저 닉네임
    @PostMapping("/signup")
    public ReturnDto signup(@RequestBody SignupDto signupDto, HttpServletRequest request) {
        ReturnDto result = memberService.signup(signupDto);
        // 회원가입 성공
        if(result.getMember() != null) {
            // 세션 생성
            HttpSession session = request.getSession(true);
            // 세션에 회원 id (long) 보관
            session.setAttribute("memberID", result.getMember().getId());
        }
        return result;
    }

    // 로그인 요청 처리
    // 성공 시 : member - 해당 유저 엔티티 / message - 해당 유저 닉네임
    // 실패 시 : member - null / message = "로그인에 실패하였습니다. 다시 시도해주세요."
    @PostMapping("/login")
    public ReturnDto login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        ReturnDto result = memberService.login(loginDto);
        // 로그인 성공
        if(result.getMember() != null) {
            // 세션 생성
            HttpSession session = request.getSession(true);
            // 세션에 회원 id (long) 보관
            session.setAttribute("memberID", result.getMember().getId());
        }
        return result;
    }

    // 로그아웃 요청 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "다음에 또 만나요!";
    }

    // 유저 정보 반환
    @GetMapping("/user")
    public MemberDto getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 현재 로그인 되어있지 않음
        if(session == null)
            return null;
        Long id = (Long)session.getAttribute("memberID");   // 세션에 보관된 유저의 id 가져오기
        MemberDto memberDto = memberService.getMemberDto(id);
        return memberDto;
    }

    // 유저 정보 수정 요청
    // 중복 아이디 사용 시 : member - 기존 유저 엔티티 / message - "이미 사용 중인 아이디입니다. 다시 시돋해주세요."
    // 중복 닉네임 사용 시 : member - 기존 유저 엔티티 / message - "이미 사용 중인 닉네임입니다. 다시 사용해주세요."
    // 성공적으로 수정 시 : member - 수정한 유저 엔티티 / message - "수정되었어요."
    @PatchMapping("/user")
    public ReturnDto updateMember(@RequestBody SignupDto signupDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 현재 로그인 되어있지 않음
        if(session == null)
            return null;
        Long id = (Long)session.getAttribute("memberID");   // 세션에 보관된 유저의 id 가져오기
        return memberService.updateMember(id, signupDto);
    }

    // 유저 소유 차량 생성 또는 수정 처리
    @PostMapping("/user/car")
    public CarDto updateCar(@RequestBody CarDto carDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 현재 로그인 되어있지 않음
        if(session == null)
            return null;
        Long id = (Long)session.getAttribute("memberID");   // 세션에 보관된 유저의 id 가져오기
        return memberService.updateCar(id, carDto);
    }

    // 회원 탈퇴 요청 처리
    @DeleteMapping("/user")
    public String deleteMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 현재 로그인 되어있지 않음
        if(session == null)
            return "로그인 되어있지 않아요.";
        Long id = (Long)session.getAttribute("memberID");   // 세션에 보관된 유저의 id 가져오기
        session.invalidate();
        memberService.deleteMember(id);
        return "회원 탈퇴 되었습니다.";
    }

}
