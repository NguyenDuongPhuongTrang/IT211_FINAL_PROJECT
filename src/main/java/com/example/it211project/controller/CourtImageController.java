package com.example.it211project.controller;

import com.example.it211project.model.dto.response.ApiResponse;
import com.example.it211project.model.dto.response.CourtImageResponse;
import com.example.it211project.service.CourtImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courts")
@RequiredArgsConstructor
public class CourtImageController {
    private final CourtImageService courtImageService;

    @PostMapping(value = "/{courtId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<CourtImageResponse>>> uploadImages(@PathVariable Long courtId,
                                                                              @RequestParam("files") List<MultipartFile> files) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Upload ảnh thành công",
                courtImageService.uploadImages(courtId, files)
        ), HttpStatus.CREATED);
    }

    @GetMapping("/{courtId}/images")
    public ResponseEntity<ApiResponse<List<CourtImageResponse>>> getImages(@PathVariable Long courtId) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy danh sách ảnh thành công",
                courtImageService.getImages(courtId)
        ), HttpStatus.OK);
    }
}
