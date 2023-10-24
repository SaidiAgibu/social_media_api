package com.saidi.social_media_plartform.response;
import com.saidi.social_media_plartform.dto.Article;
import lombok.Data;

import java.util.List;

@Data
public class NewsApiResponse {
        private String status;
        private int totalResults;
        private List<Article> articles;
}
