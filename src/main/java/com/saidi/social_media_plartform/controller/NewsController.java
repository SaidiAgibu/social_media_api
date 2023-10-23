package com.saidi.social_media_plartform.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@Data
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsController {



    @GetMapping("/news")
    public ResponseEntity<?> viewNews() {
        return ResponseEntity.ok().body("");
    }
}
