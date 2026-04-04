package com.authserver.authserver.url_shortner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.url_shortner.model.UrlShortner;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UrlShortnerRepository extends JpaRepository<UrlShortner, Long> {
    boolean existsByShortCode(String shortCode);
    Optional<UrlShortner> findByShortCode(String shortCode);
    Optional<UrlShortner> findByOriginalUrl(String originalUrl);
    @Modifying
@Query("update UrlShortner u set u.clickCount = :count where u.shortCode = :shortCode")
void updateClickCount(@Param("shortCode") String shortCode,
                      @Param("count") Long count);
}
