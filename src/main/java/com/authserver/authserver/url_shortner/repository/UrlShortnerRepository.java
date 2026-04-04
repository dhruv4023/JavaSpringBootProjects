package com.authserver.authserver.url_shortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.url_shortner.model.UrlShortner;

@Repository
public interface UrlShortnerRepository extends JpaRepository<UrlShortner, Long> {
    boolean existsByShortCode(String shortCode);
    Optional<UrlShortner> findByShortCode(String shortCode);
    Optional<UrlShortner> findByOriginalUrl(String originalUrl);
}
