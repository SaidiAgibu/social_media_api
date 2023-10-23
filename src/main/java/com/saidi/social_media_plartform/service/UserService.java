package com.saidi.social_media_plartform.service;

import com.saidi.social_media_plartform.dto.*;
import com.saidi.social_media_plartform.exceptions.AlreadyExistsException;
import com.saidi.social_media_plartform.exceptions.NotFoundException;
import com.saidi.social_media_plartform.models.Follows;
import com.saidi.social_media_plartform.models.Posts;
import com.saidi.social_media_plartform.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    public ResponseEntity<String> registerUser(RegisterDto registerDto) throws AlreadyExistsException;

    public ResponseEntity<String> verifyAccount(String token);

    public ResponseEntity<LoginResponse> loginUser(LoginDto loginDto);

    public ResponseEntity<String> uploadProfilePicture(Long id, MultipartFile multipartFile) throws IOException, NotFoundException;


    public ResponseEntity<String> changePassword(Long id, ChangePasswordDto changePasswordDto) throws NotFoundException;

    public ResponseEntity<?> addBio(Long id,BioDto bioDto) throws NotFoundException;

    public ResponseEntity<ProfileDto> getUserProfile(Long id) throws NotFoundException;

    public ResponseEntity<PostDto> createPost(String content, MultipartFile multipartFile) throws NotFoundException, IOException;

    public ResponseEntity<String> deletePost(Long id) throws NotFoundException;

    public ResponseEntity<PostDto> viewPost(Long id) throws NotFoundException;

    public ResponseEntity<List<PostDto>> viewPostsRelatedToUser(Long id) throws NotFoundException;

    public ResponseEntity<?> likeAPost(Long postId) throws NotFoundException, AlreadyExistsException;

    public ResponseEntity<?> viewLikesOfAPost(Long id) throws NotFoundException;

    public ResponseEntity<String> commentPost(Long id, CommentDto commentDto) throws NotFoundException;

    public ResponseEntity<List<ViewCommentDto>> viewComments(Long postId) throws NotFoundException;

    public ResponseEntity<String> deleteComment(Long id) throws NotFoundException;


    public ResponseEntity<List<SearchUser>> searchUser(String username) throws NotFoundException;

    public ResponseEntity<?> followUser(String username) throws NotFoundException;

    public ResponseEntity<List<String>> getFollowers() throws NotFoundException;

    public ResponseEntity<List<String>> getFollowing() throws NotFoundException;

    public ResponseEntity<?> unFollowUser(String username) throws NotFoundException;
}
