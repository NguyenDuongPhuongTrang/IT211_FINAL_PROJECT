package com.example.it211project.repository;

import com.example.it211project.model.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Long> {
    boolean existsByToken(String token);
}
