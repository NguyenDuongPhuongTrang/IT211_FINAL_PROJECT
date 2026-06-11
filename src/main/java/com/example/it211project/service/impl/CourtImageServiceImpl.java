package com.example.it211project.service.impl;

import com.example.it211project.exception.ResourceNotFoundException;
import com.example.it211project.model.dto.response.CourtImageResponse;
import com.example.it211project.model.entity.Court;
import com.example.it211project.model.entity.CourtImage;
import com.example.it211project.repository.CourtImageRepository;
import com.example.it211project.repository.CourtRepository;
import com.example.it211project.service.CourtImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourtImageServiceImpl implements CourtImageService {
    private final CourtRepository courtRepository;
    private final CourtImageRepository courtImageRepository;

    @Value("${imageUploadDir}")
    private String imageUploadDir;

    @Override
    public List<CourtImageResponse> uploadImages(Long courtId, List<MultipartFile> files) {
        Court court = courtRepository.findById(courtId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));
        File folder = new File(imageUploadDir);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return files.stream().map(file -> {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get(imageUploadDir + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                CourtImage image = CourtImage.builder()
                        .imageUrl("/images/" + fileName)
                        .court(court)
                        .build();
                courtImageRepository.save(image);
                return CourtImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .build();
            } catch (Exception e) {
                throw new RuntimeException("Upload ảnh thất bại");
            }
        }).toList();
    }

    @Override
    public List<CourtImageResponse> getImages(Long courtId) {
        return courtImageRepository
                .findByCourtId(courtId)
                .stream()
                .map(image ->
                        CourtImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .build()
                )
                .toList();
    }
}
