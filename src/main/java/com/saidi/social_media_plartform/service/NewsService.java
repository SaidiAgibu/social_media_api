package com.saidi.social_media_plartform.service;



import com.saidi.social_media_plartform.dto.Article;
import com.saidi.social_media_plartform.dto.NewsDto;
import com.saidi.social_media_plartform.response.NewsApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsService {

    private static final String API_URL = "https://newsapi.org/v2/everything?q=keyword&apiKey=a369658e7b574e5f96fd12fd2f42f423";

    public List<NewsDto> getNews() {
        RestTemplate restTemplate = new RestTemplate();
        NewsApiResponse response = restTemplate.getForObject(API_URL, NewsApiResponse.class);

        if (response != null && response.getArticles() != null) {
            return response.getArticles().stream()
                    .map(this::mapToNewsDto)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private NewsDto mapToNewsDto(Article article) {
        return NewsDto.builder()
                .name(article.getSource().getName())
                .author(article.getAuthor())
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .imageUrl(article.getUrlToImage())
                .build();
    }
}