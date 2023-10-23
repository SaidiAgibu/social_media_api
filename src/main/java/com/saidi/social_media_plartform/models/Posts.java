package com.saidi.social_media_plartform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(name = "image_or_video_url", nullable = true)
    private String imageUrlOrVideoUrl;
    @Column(name = "text_content", nullable = false)
    private String content;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

}
