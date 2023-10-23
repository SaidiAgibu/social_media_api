package com.saidi.social_media_plartform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private String username;
    private String email;
    private String bio;
    private String photoUrl;
}
