package com.saidi.social_media_plartform.dto;

import com.saidi.social_media_plartform.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String username;
    private String imageOrVideoUrl;
    private String content;
    private LocalDateTime createdAt;
}
