package com.saidi.social_media_plartform.service;

import com.saidi.social_media_plartform.config.JWTGenerator;
import com.saidi.social_media_plartform.dto.*;
import com.saidi.social_media_plartform.exceptions.AlreadyExistsException;
import com.saidi.social_media_plartform.exceptions.NotFoundException;
import com.saidi.social_media_plartform.models.*;
import com.saidi.social_media_plartform.repository.*;
import com.saidi.social_media_plartform.response.LoginResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Data
@Service
public class UserServiceImplementation implements UserService{



    private FollowerRepository followerRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ConfirmationRepository confirmationRepository;
    private ProfileImageRepository profileImageRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;
    private CloudinaryImageService cloudinaryImageService;
    private PostRepository postRepository;
    private LikeRepository likeRepository;

    @Autowired
    public UserServiceImplementation(FollowerRepository followerRepository, CommentRepository commentRepository,LikeRepository likeRepository,PostRepository postRepository,UserRepository userRepository, RoleRepository roleRepository, ConfirmationRepository confirmationRepository, ProfileImageRepository profileImageRepository, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator,CloudinaryImageService cloudinaryImageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.confirmationRepository = confirmationRepository;
        this.profileImageRepository = profileImageRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
        this.cloudinaryImageService = cloudinaryImageService;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.followerRepository = followerRepository;


    }


    @Override
    public ResponseEntity<String> registerUser(RegisterDto registerDto) throws AlreadyExistsException {
        Optional<User> userExists = userRepository.findByUsernameOrEmailIgnoreCase(registerDto.getUsername(), registerDto.getEmail());
        if(userExists.isPresent()) {
            throw new AlreadyExistsException("User Already exists");
        }
        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setEnabled(false);
        Role role = roleRepository.findByRoleName("ROLE_USER");
        if(role == null) {
            role = checkRoleExists();
        }
        newUser.setRoles(Arrays.asList(role));
        userRepository.save(newUser);

        Confirmation confirmation = new Confirmation(newUser);
        confirmationRepository.save(confirmation);

        /* Sending email to the client */
        emailService.sentEmailToUser(newUser.getFirstName(), newUser.getEmail(), confirmation.getToken());
        return new ResponseEntity<>("Please check your email to verify your account", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyAccount(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail()).get();
        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseEntity<>("Account verified successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> uploadProfilePicture(Long id, MultipartFile multipartFile) throws IOException, NotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User user = userOptional.get();
        log.info("this the user {}", user);
        String fileName = cloudinaryImageService.uploadImage(multipartFile);
        log.info("the image url is {}", fileName);
        user.setProfileImageUrl(fileName);
        userRepository.save(user);

        return ResponseEntity.ok().body("Profile picture updated successfully");
    }





    @Override
    public ResponseEntity<String> changePassword(Long id, ChangePasswordDto changePasswordDto) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw  new NotFoundException("User not found");
        }
        User userExists = user.get();
        if(passwordEncoder.matches(userExists.getPassword(), changePasswordDto.getOldPassword())) {
            userExists.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(userExists);
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password.");
        }

    }

    @Override
    public ResponseEntity<?> addBio(Long id, BioDto bioDto) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw  new NotFoundException("User not found");
        }
        User userExists = user.get();
        userExists.setBio(bioDto.getBio());
        userRepository.save(userExists);
        return ResponseEntity.ok("Bio added successfully.");
    }

    @Override
    public ResponseEntity<ProfileDto> getUserProfile(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw  new NotFoundException("User not found");
        }
        User userExists = user.get();
        ProfileDto profile = ProfileDto.builder()
                .username(userExists.getUsername())
                .email(userExists.getEmail())
                .bio(userExists.getBio())
                .photoUrl(userExists.getProfileImageUrl())
                .build();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PostDto> createPost(String content, MultipartFile multipartFile) throws NotFoundException, IOException {
        String photoUrlOrVideoUrl = cloudinaryImageService.uploadImage(multipartFile);

        Posts post = new Posts();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("User not found"));
            post.setUser(loggedInUser);
        }
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setImageUrlOrVideoUrl(photoUrlOrVideoUrl);
        postRepository.save(post);

        PostDto postDto = mapToPostDto(post);


        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deletePost(Long id) throws NotFoundException {
        Optional<Posts> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        postRepository.delete(post.get());
        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PostDto> viewPost(Long id) throws NotFoundException {
        Optional<Posts> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        PostDto postDto = mapToPostDto(post.get());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PostDto>> viewPostsRelatedToUser(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            List<PostDto> posts = postRepository.findAll().stream()
                    .map(this::mapToPostDto).collect(Collectors.toList());

            return new ResponseEntity<>(posts, HttpStatus.OK);

        }

        throw new NotFoundException("Posts not found");

    }

    @Override
    public ResponseEntity<?> likeAPost(Long postId) throws NotFoundException, AlreadyExistsException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("User not found"));

            Optional<Posts> post = postRepository.findById(postId);
            if(post.isEmpty()) {
                throw new NotFoundException("Post not found");
            }

            Likes existingLike = likeRepository.findByPostAndLiker(post.get(), loggedInUser);
            if (existingLike != null) {
                throw new AlreadyExistsException("You already liked this post, you can't like again!!");
            }

            Likes newLike = new Likes();
            newLike.setPost(post.get());
            newLike.setLiker(loggedInUser);
            likeRepository.save(newLike);

            return new ResponseEntity<>("You liked this post", HttpStatus.OK);
        }

        throw new NotFoundException("User not found");
    }


    @Override
    public ResponseEntity<?> viewLikesOfAPost(Long id) throws NotFoundException {
        Optional<Posts> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        Long likes = likeRepository.count();
        List<Likes> likesList = likeRepository.findAll();
        List<String> likers = likesList.stream()
                .map(people -> people.getLiker().getUsername()).toList();


        return ResponseEntity.ok("There are " + likes + " likes and the likers are : " + likers);
    }

    @Override
    public ResponseEntity<String> commentPost(Long id, CommentDto commentDto) throws NotFoundException {
        Optional<Posts> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        Comments comments = new Comments();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("User not found"));
            comments.setCommenter(loggedInUser);

        }
        comments.setComment(commentDto.getComment());
        comments.setPost(post.get());
        comments.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comments);

        return ResponseEntity.ok().body("Comment added successfully");
    }

    @Override
    public ResponseEntity<List<ViewCommentDto>> viewComments(Long postId) throws NotFoundException {
        Optional<Posts> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        List<ViewCommentDto> comments = commentRepository.findAll().stream()
                .map(this::mapToCommentDto).toList();

        return ResponseEntity.ok().body(comments);

    }

    @Override
    public ResponseEntity<String> deleteComment(Long id) throws NotFoundException {
        Optional<Comments> comments = commentRepository.findById(id);
        if(comments.isEmpty()) {
            throw  new NotFoundException("Post not found");
        }
        commentRepository.delete(comments.get());
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SearchUser>> searchUser(String username) throws NotFoundException {
        List<User> user = userRepository.findByUsernameIgnoreCase(username);
        if(!user.isEmpty()) {
            List<SearchUser> response = user.stream()
                    .map(this::mapToSearchUserDto).toList();
            return ResponseEntity.ok().body(response);
        }

        throw new NotFoundException("No user found");
    }

    @Override
    public ResponseEntity<?> followUser(String username) throws NotFoundException {
        User userToFollow = userRepository.findByUsername(username);
        if (userToFollow != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                        .orElseThrow(() -> new NotFoundException("User not found"));

                if (!userToFollow.equals(loggedInUser)) { // Checking if the user is not trying to follow themselves
                    Follows follow = new Follows();
                    follow.setFollowing(userToFollow);
                    follow.setFollower(loggedInUser);

                    loggedInUser.getFollowing().add(follow);
                    userToFollow.getFollowers().add(follow);

                    userRepository.save(loggedInUser);
                    userRepository.save(userToFollow);

                    return ResponseEntity.ok("Successfully followed user: " + username);
                } else {
                    return ResponseEntity.badRequest().body("Cannot follow yourself.");
                }
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @Override
    public ResponseEntity<List<String>> getFollowers() throws NotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (loggedInUser != null) {
                List<Follows> followsList = followerRepository.findAll();
                long totalFollowers = followsList.size();
                List<String> followerUsernames = followsList.stream()
                        .map(followers -> followers.getFollower().getUsername())
                        .collect(Collectors.toList());
                return ResponseEntity.ok().body(followerUsernames);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
    }

    @Override
    public ResponseEntity<List<String>> getFollowing() throws NotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User loggedInUser = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (loggedInUser != null) {
                List<Follows> followsList = followerRepository.findAll();
                List<String> followingUsernames = followsList.stream()
                        .map(followers -> followers.getFollowing().getUsername())
                        .collect(Collectors.toList());
                return ResponseEntity.ok().body(followingUsernames);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
    }

    @Override
    public ResponseEntity<?> unFollowUser(String username) throws NotFoundException {
       
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");



    }


    private SearchUser mapToSearchUserDto(User user) {
        SearchUser searchUser = new SearchUser();
        searchUser.setUsername(user.getUsername());
        searchUser.setBio(user.getBio());
        searchUser.setPhotoUrl(user.getProfileImageUrl());

        return searchUser;
    }


    private Role checkRoleExists() {
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        return  roleRepository.save(role);
    }

    public PostDto mapToPostDto(Posts post) {
        PostDto postDto = new PostDto();
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUser().getUsername());
        postDto.setImageOrVideoUrl(post.getImageUrlOrVideoUrl());
        postDto.setCreatedAt(post.getCreatedAt());

        return postDto;

    }

    public ViewCommentDto mapToCommentDto(Comments comments) {
        ViewCommentDto viewCommentDto = new ViewCommentDto();
        viewCommentDto.setComment(comments.getComment());
        viewCommentDto.setCommenter(comments.getCommenter().getUsername());
        viewCommentDto.setCreatedAt(comments.getCreatedAt());
        return viewCommentDto;
    }




}
