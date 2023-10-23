package com.saidi.social_media_plartform.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sentEmailToUser(String name, String to, String token);
}
