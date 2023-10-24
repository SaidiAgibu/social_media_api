package com.saidi.social_media_plartform.controller;

import com.saidi.social_media_plartform.dto.MessageDto;
import com.saidi.social_media_plartform.exceptions.NotFoundException;
import com.saidi.social_media_plartform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {


    private final UserService userService;
    @MessageMapping("send-msg-at")

    @SendTo("/topic/receive-msg-at")
    public ResponseEntity<?> sentMessage(Long id, MessageDto messageDto) throws NotFoundException{
        return userService.sentMessage(id, messageDto);
    }

}
