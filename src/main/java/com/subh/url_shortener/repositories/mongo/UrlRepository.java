package com.subh.url_shortener.repositories.mongo;

import com.subh.url_shortener.shared.schemas.UrlSchema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlSchema, String> {
    UrlSchema findByOriginalUrl(String originalUrl);
}
