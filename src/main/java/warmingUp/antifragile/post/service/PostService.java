package warmingUp.antifragile.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import warmingUp.antifragile.post.domain.Post;
import warmingUp.antifragile.post.dto.PostThumbnailDto;
import warmingUp.antifragile.post.dto.ReturnManyDto;
import warmingUp.antifragile.post.dto.ReturnOneDto;
import warmingUp.antifragile.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    public ReturnManyDto<PostThumbnailDto> getThumbnails(){
        List<PostThumbnailDto> thumbnails = postRepository.findThumbnails();
        return new ReturnManyDto<PostThumbnailDto>((ArrayList<PostThumbnailDto>)thumbnails,"조회성공" );
    }

    public ReturnOneDto<Post> getPostById(Long postId){
        if(postId == null)
            return new ReturnOneDto<Post>(null,"null값을 postId로 받았습니다");
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null)
            return new ReturnOneDto<Post>(null, "해당 리뷰를 찾을 수 없습니다");
        return new ReturnOneDto<Post>(post, "리뷰 불러오기 성공");

    }

    public ReturnOneDto<> createPost(memberId, postCreateDto);


}
