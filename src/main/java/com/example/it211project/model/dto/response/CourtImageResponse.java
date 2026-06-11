package com.example.it211project.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtImageResponse {
    private Long id;
    private String imageUrl;
}
