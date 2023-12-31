package com.saidi.social_media_plartform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel {

    private UUID id;
    private String content;
    private String sender;
    private String receiver;

}
