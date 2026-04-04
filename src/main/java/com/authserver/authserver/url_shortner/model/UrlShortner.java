package com.authserver.authserver.url_shortner.model;

import com.authserver.authserver.base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "url_shortner", indexes = {
    @Index(name = "idx_short_code", columnList = "shortCode")
    , @Index(name = "idx_original_url", columnList = "originalUrl")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class UrlShortner extends BaseModel {

    @Column(nullable = false, length = 2048, unique = true)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private Long clickCount;

    @Column
    private Long expiryAt;
}
