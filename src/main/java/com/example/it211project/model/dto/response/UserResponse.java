package com.example.it211project.model.dto.response;

import com.example.it211project.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean active;
}
