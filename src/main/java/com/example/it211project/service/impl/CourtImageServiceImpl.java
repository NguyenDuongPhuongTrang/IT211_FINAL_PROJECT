package com.example.it211project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.it211project.exception.ResourceNotFoundException;
import com.example.it211project.model.dto.response.CourtImageResponse;
import com.example.it211project.model.entity.Court;
import com.example.it211project.model.entity.CourtImage;
import com.example.it211project.repository.CourtImageRepository;
import com.example.it211project.repository.CourtRepository;
import com.example.it211project.service.CourtImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourtImageServiceImpl implements CourtImageService {
    private final CourtRepository courtRepository;
    private final CourtImageRepository courtImageRepository;
    private final Cloudinary cloudinary;

    @Override
    public List<CourtImageResponse> uploadImages(Long courtId, List<MultipartFile> files) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));

        return files.stream().map(file -> {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("folder", "courts/" + courtId)
                );
                String imageUrl = (String) uploadResult.get("secure_url");

                CourtImage image = CourtImage.builder()
                        .imageUrl(imageUrl)
                        .court(court)
                        .build();
                courtImageRepository.save(image);

                return CourtImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Upload ảnh lên Cloudinary thất bại: " + e.getMessage());
            }
        }).toList();
    }

    @Override
    public List<CourtImageResponse> getImages(Long courtId) {
        return courtImageRepository
                .findByCourtId(courtId)
                .stream()
                .map(image -> CourtImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .toList();
    }
}