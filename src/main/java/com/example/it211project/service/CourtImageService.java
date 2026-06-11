package com.example.it211project.service;

import com.example.it211project.model.dto.response.CourtImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourtImageService {
    List<CourtImageResponse> uploadImages(Long courtId, List<MultipartFile> files);
    List<CourtImageResponse> getImages(Long courtId);
}
