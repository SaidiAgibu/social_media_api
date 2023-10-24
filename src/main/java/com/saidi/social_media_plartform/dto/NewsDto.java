package com.saidi.social_media_plartform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDto {
    private String name;
    private String author;
    private String title;
    private String description;
    private String content;
    private String imageUrl;
}
