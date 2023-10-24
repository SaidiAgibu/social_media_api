package com.saidi.social_media_plartform.controller;

import com.saidi.social_media_plartform.dto.NewsDto;
import com.saidi.social_media_plartform.service.NewsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Data
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsController {


    private final NewsService newsService;

    @GetMapping("/news")
    public List<NewsDto> viewNews() {
        return newsService.getNews();
    }
}
