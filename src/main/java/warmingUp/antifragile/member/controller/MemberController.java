package warmingUp.antifragile.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import warmingUp.antifragile.car.dto.CarDto;
import warmingUp.antifragile.comment.dto.CommentSendDto;
import warmingUp.antifragile.member.dto.LoginDto;
import warmingUp.antifragile.member.dto.MemberDto;
import warmingUp.antifragile.member.dto.ReturnDto;
import warmingUp.antifragile.member.dto.SignupDto;
import warmingUp.antifragile.member.service.MemberService;
import warmingUp.antifragile.post.dto.PostThumbnailDto;
import warmingUp.antifragile.post.dto.ReturnManyDto;
import warmingUp.antifragile.post.dto.ReturnOneDto;

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
    public ReturnDto logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new ReturnDto(null, "다음에 또 만나요!");
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
    public ReturnDto deleteMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 현재 로그인 되어있지 않음
        if(session == null)
            return new ReturnDto(null, "로그인 되어있지 않아요.");
        Long id = (Long)session.getAttribute("memberID");   // 세션에 보관된 유저의 id 가져오기
        session.invalidate();
        memberService.deleteMember(id);
        return new ReturnDto(null, "회원 탈퇴 되었습니다.");
    }

    ////////////////////////////////////////아래서부턴 Test 3에서 생성됨

    //유저가 작성한 게시물의 썸네일들을 반환
    @GetMapping("/user/reviews")
    public ReturnManyDto<PostThumbnailDto>getReviewByMemberId(HttpServletRequest request){
        //로그인이 안되어 있을 때
        HttpSession session = request.getSession(false);
        if(session == null)
            return new ReturnManyDto<>(null, "로그인을 하세요");

        Long memberId = (Long)session.getAttribute("memberID");
        if(memberId == null)
            return new ReturnManyDto<>(null, "로그인을 하세요");
        //로그인이 되어 있을 때
        return memberService.getReviewByMemberId(memberId);
    }

    @GetMapping("/user/comments")
    public ReturnManyDto<CommentSendDto>getCommentByMemberId(HttpServletRequest request) {
        //로그인이 안되어 있을 때
        HttpSession session = request.getSession(false);
        if (session == null)
            return new ReturnManyDto<>(null, "로그인을 하세요");

        Long memberId = (Long) session.getAttribute("memberID");
        if (memberId == null)
            return new ReturnManyDto<>(null, "로그인을 하세요");
        //로그인이 되어 있을 때
        return memberService.getCommentByMemberId(memberId);
    }
}
