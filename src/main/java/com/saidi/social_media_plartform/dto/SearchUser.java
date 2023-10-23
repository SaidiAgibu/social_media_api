package com.saidi.social_media_plartform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUser {
    private String username;
    private String bio;
    private String photoUrl;
}
