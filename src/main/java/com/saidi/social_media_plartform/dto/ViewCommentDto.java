package com.saidi.social_media_plartform.dto;

import com.saidi.social_media_plartform.models.Posts;
import com.saidi.social_media_plartform.models.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCommentDto {
    private String comment;
    private String commenter;
    private LocalDateTime createdAt;
}
