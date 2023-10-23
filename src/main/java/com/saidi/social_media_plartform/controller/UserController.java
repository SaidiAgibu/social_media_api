package com.saidi.social_media_plartform.controller;

import com.saidi.social_media_plartform.dto.*;
import com.saidi.social_media_plartform.exceptions.AlreadyExistsException;
import com.saidi.social_media_plartform.exceptions.NotFoundException;
import com.saidi.social_media_plartform.models.Follows;
import com.saidi.social_media_plartform.service.UserService;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Data
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/bio")
    public ResponseEntity<?> addBio(@PathVariable("id") Long id,@RequestBody BioDto bioDto) throws NotFoundException {
        return userService.addBio(id,bioDto);
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable("id") Long id,@RequestParam("image") MultipartFile multipartFile) throws IOException, NotFoundException {
        return userService.uploadProfilePicture(id,multipartFile);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id,@RequestBody ChangePasswordDto changePasswordDto) throws NotFoundException {
        return userService.changePassword(id,changePasswordDto);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable("id") Long id) throws NotFoundException {
        return userService.getUserProfile(id);
    }

    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestParam("text") String content, @RequestParam("post") MultipartFile multipartFile ) throws NotFoundException, IOException {
        return userService.createPost(content, multipartFile);
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) throws NotFoundException {
        return userService.deletePost(id);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> viewPost(@PathVariable("id") Long id) throws NotFoundException {
        return userService.viewPost(id);
    }

    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<PostDto>> viewPostsRelatedToUser(@PathVariable("userId") Long id) throws NotFoundException {
        return userService.viewPostsRelatedToUser(id);
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<?> likeAPost(@PathVariable("postId") Long postId) throws NotFoundException, AlreadyExistsException {
        return userService.likeAPost(postId);
    }
    @GetMapping("/post/{postId}/likes")
    public ResponseEntity<?> viewLikesOfAPost(@PathVariable("postId") Long id) throws NotFoundException {
        return userService.viewLikesOfAPost(id);
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<String> commentPost(@PathVariable("postId") Long id, @RequestBody CommentDto commentDto) throws NotFoundException {
        return userService.commentPost(id, commentDto);
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<ViewCommentDto>> viewComments(@PathVariable("postId") Long postId) throws NotFoundException {
        return userService.viewComments(postId);
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") Long id) throws NotFoundException {
        return userService.deleteComment(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchUser>> searchUser(@RequestParam("username") String username) throws NotFoundException {
        return userService.searchUser(username);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(@RequestParam("username") String username) throws NotFoundException {
        return userService.followUser(username);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<String>>  getFollowers() throws NotFoundException {
        return userService.getFollowers();
    }

    @GetMapping("/following")
    public ResponseEntity<List<String>>  getFollowing() throws NotFoundException {
        return userService.getFollowing();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unFollowUser(@RequestParam("username") String username) throws NotFoundException {
        return userService.unFollowUser(username);
    }









}
