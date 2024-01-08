package warmingUp.antifragile.member.service;

import jakarta.persistence.PostRemove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import warmingUp.antifragile.car.domain.Car;
import warmingUp.antifragile.car.domain.Model;
import warmingUp.antifragile.car.dto.CarDto;
import warmingUp.antifragile.car.repository.CarRepository;
import warmingUp.antifragile.car.repository.ModelRepository;
import warmingUp.antifragile.comment.domain.Comment;
import warmingUp.antifragile.comment.dto.CommentSendDto;
import warmingUp.antifragile.comment.repository.CommentRepository;
import warmingUp.antifragile.member.domain.Member;
import warmingUp.antifragile.member.dto.LoginDto;
import warmingUp.antifragile.member.dto.MemberDto;
import warmingUp.antifragile.member.dto.ReturnDto;
import warmingUp.antifragile.member.dto.SignupDto;
import warmingUp.antifragile.member.repository.MemberRepository;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.PostThumbnailDto;
import warmingUp.antifragile.post.dto.ReturnManyDto;
import warmingUp.antifragile.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ModelRepository modelRepository;

    // 회원가입 요청 처리
    public ReturnDto signup(SignupDto signupDto) {
        // 아이디 중복 체크
        Member member = memberRepository.findByLoginId(signupDto.getLoginId()).orElse(null);
        if(member != null)
            return new ReturnDto(null, "이미 사용 중인 아이디입니다. 다시 시도해주세요.");
        // 닉네임 중복 체크
        member = memberRepository.findByNickname(signupDto.getNickname()).orElse(null);
        if(member != null)
            return new ReturnDto(null, "이미 사용 중인 닉네임입니다. 다시 시도해주세요.");
        // 성공적인 회원가입 처리
        member = signupDto.toEntity();
        Member saved = memberRepository.save(member);
        return new ReturnDto(saved, saved.getNickname());
    }

    // 로그인 요청 처리
    public ReturnDto login(LoginDto loginDto) {
        Member member = memberRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword()).orElse(null);
        if(member == null)
            return new ReturnDto(null, "로그인에 실패하였습니다. 다시 시도해주세요.");
        return new ReturnDto(member, member.getNickname());
    }

    // 해당 id를 가지는 member dto 반환
    public MemberDto getMemberDto(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null)
            return null;
        MemberDto memberDto = new MemberDto(member.getId(), member.getLoginId(), member.getPassword(),
                member.getNickname(), null, null,null, null, null);
        if(member.getCarId() != null) {
            Car car = carRepository.findById(member.getCarId()).orElse(null);
            if(car == null)
                return memberDto;
            Model model = modelRepository.findById(car.getModelId()).orElse(null);
            if(model == null)
                return memberDto;
            memberDto.setCarNum(car.getCarNum());
            memberDto.setModelName(model.getName());
            memberDto.setCarAge(car.getCarAge());
            memberDto.setBuyYear(car.getBuyYear());
            memberDto.setBuyMonth(car.getBuyMonth());
        }
        return memberDto;
    }

    // 유저 정보 수정 요청
    public ReturnDto updateMember(Long id, SignupDto signupDto) {
        // 기존의 멤버 엔티티 가져오기
        Member original = memberRepository.findById(id).orElse(null);
        if(original == null)
            return new ReturnDto(null, "존재하지 않는 유저입니다.");

        // 아이디 중복 확인
        Member member = memberRepository.findByLoginId(signupDto.getLoginId()).orElse(null);
        if(member != null)
            return new ReturnDto(original, "이미 사용 중인 아이디입니다. 다시 시돋해주세요.");

        // 닉네임 중복 확인
        member = memberRepository.findByNickname(signupDto.getNickname()).orElse(null);
        if(member != null)
            return new ReturnDto(original, "이미 사용 중인 닉네임입니다. 다시 사용해주세요.");

        // 성공적인 수정 진행
        original.setLoginId(signupDto.getLoginId());
        original.setPassword(signupDto.getPassword());
        original.setNickname(signupDto.getNickname());
        Member updpateMember = memberRepository.save(original);
        return new ReturnDto(updpateMember, "수정되었어요.");
    }

    // 유저의 차량 정보 생성 또는 수정
    public CarDto updateCar(Long id, CarDto carDto) {
        Model model = modelRepository.findByName(carDto.getModelName()).orElse(null);
        if(model == null)
            return null;
        // 제공받은 정보를 바탕으로 새로운 Car 객체 생성
        Car car = new Car(null, carDto.getCarNum(), model.getId(),
                carDto.getCarAge(), carDto.getBuyYear(), carDto.getBuyMonth());
        Car carSaved = carRepository.save(car);
        // 유저의 엔티티 속 소유 차량 id 수정하기
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null)
            return null;
        member.setCarId(carSaved.getId());
        memberRepository.save(member);
        carDto.setId(carSaved.getId());
        return carDto;
    }

    // 회원 탈퇴 처리
    public void deleteMember(Long id) {
        // 작성한 게시물 전부 삭제
        postRepository.deleteAllByWriterId(id);
        // 작성한 댓글 전부 삭제
        commentRepository.deleteAllByWriterId(id);
        // 유저 정보 삭제
        Member member = memberRepository.findById(id).orElse(null);
        if(member != null)
            memberRepository.delete(member);
    }


    //////////////////////////아래서부턴 test3에서 생성됨

    //유저가 작성한 게시글 목록 컨트롤러에게 전달
    public ReturnManyDto<PostThumbnailDto> getReviewByMemberId(Long memberId){
        //유저ID로 Post 인스턴스를 리스트에 저장
        List<Post> posts = postRepository.findAllByWriterId(memberId);
        ArrayList<PostThumbnailDto> postThumbnailDtos = new ArrayList<>();

        //순회하며 PostThumbnailDto로 변환 (썸네일 생성중 문제가 생기는 인스턴스는 무시한다)
        for(Post post : posts){
            Member member = memberRepository.findById(post.getWriterId()).orElse(null);
            if(member == null)
                continue;
            Car car = carRepository.findById(member.getCarId()).orElse(null);
            if(car == null)
                continue;
            Model model = modelRepository.findById(car.getModelId()).orElse(null);
            if(model == null)
                continue;
            PostThumbnailDto postThumbnailDto = PostThumbnailDto.fromEntities(post, model, member, car);
            postThumbnailDtos.add(postThumbnailDto);
        }

        //최신순으로 정렬 후 컨트롤러에 전달
        postThumbnailDtos.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<>(postThumbnailDtos,"조회성공" );
    }

    // 유저가 작성한 댓글 목록을 컨트롤러에게 전달
    public ReturnManyDto<CommentSendDto> getCommentByMemberId(Long memberId){

        // 댓글 엔티티에서 postId로 필터링, 필터링 후 for문으로 Dto로 변환
        List<Comment> comments = commentRepository.findAllByWriterId(memberId);
        ArrayList<CommentSendDto> commentSendDtos = new ArrayList<>();
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null)
            return new ReturnManyDto<>(null,"맴버 조회 실패");

        for(Comment comment: comments){

            Post post = postRepository.findById(comment.getPostId()).orElse(null);
            if(post == null)// post 조회실패시 해당 댓글은 추가하지 않는다
                continue;

            CommentSendDto commentSendDto = CommentSendDto.fromEntities(post, comment, member);
            commentSendDtos.add(commentSendDto);
        }

        //최신순으로 정렬 후 컨트롤러에 전달
        commentSendDtos.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        return new ReturnManyDto<>(commentSendDtos,"조회성공" );
    }


}
