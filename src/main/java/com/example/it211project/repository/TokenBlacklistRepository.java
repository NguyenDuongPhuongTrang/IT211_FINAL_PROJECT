package com.example.it211project.repository;

import com.example.it211project.model.entity.TokenBlacklist;
import org.springframework.data.repository.CrudRepository;

public interface TokenBlacklistRepository extends CrudRepository<TokenBlacklist, String> {
    boolean existsByToken(String token);
}