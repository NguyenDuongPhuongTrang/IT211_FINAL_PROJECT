package com.example.it211project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("token_blacklist")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenBlacklist {
    @Id
    private String id;

    @Indexed
    private String token;

    @TimeToLive
    private Long ttl;
}